package divinerpg.dimensions.arcana;

import java.util.List;
import java.util.Random;

import divinerpg.capabilities.arcana.Arcana;
import divinerpg.dimensions.arcana.mazegen.ArcanaMazeGenerator;
import divinerpg.dimensions.arcana.mazegen.Cell;
import divinerpg.dimensions.arcana.mazegen.MazeMapMemoryStorage;
import divinerpg.registry.StructureRegistry;
import divinerpg.structure.arcana.ArcanaStructureHandler;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

public class ChunkGeneratorArcana implements IChunkGenerator {
    private final Random rand;
    private final World world;
    private Biome[] biomesForGeneration;

    public ChunkGeneratorArcana(World world, long seed) {
        this.world = world;
        this.rand = new Random(seed);
    }

    @Override
    public Chunk generateChunk(int x, int z) {
        this.rand.setSeed((long) x * 341873128712L + (long) z * 132897987541L);
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16,
                16);

        Chunk chunk = new Chunk(this.world, new ChunkPrimer(), x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte) Biome.getIdForBiome(this.biomesForGeneration[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        Biome biome = this.world.getBiomeProvider().getBiome(pos);

        return biome != null ? biome.getSpawnableList(creatureType) : null;
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int chunkX, int chunkZ) {
        return false;
    }

    @Override
    public void recreateStructures(Chunk p_180514_1_, int x, int z) {
    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position,
            boolean findUnexplored) {
        return null;
    }

    @Override
    public void populate(int chunkX, int chunkZ) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        BlockPos pos = new BlockPos(x, 0, z);
        Biome biome = this.world.getBiome(pos.add(16, 0, 16));

        this.rand.setSeed(this.world.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long) chunkX * k + (long) chunkZ * l ^ this.world.getSeed());

        //this.rand is not used for overall maze generation which is specific to the region root instead of the specific chunk
        //However, it will be used to determine per-chunk things like the specific room variant picked, as well as randomize specific things within a chunk

        //MAZE GENERATION
        Cell[][] mazeMap;

        int regionRootX, regionRootZ;
        int mapCoordinateX, mapCoordinateZ;
        regionRootX = roundUpToMultiple(chunkX, ArcanaMazeGenerator.MAZE_SIZE);
        regionRootZ = roundUpToMultiple(chunkZ, ArcanaMazeGenerator.MAZE_SIZE);
        
        ChunkPos regionRoot = new ChunkPos(regionRootX, regionRootZ);
        Cell[][] storedGrid = MazeMapMemoryStorage.getMapForChunkPos(regionRoot);
        if(storedGrid == null) {
            mazeMap = ArcanaMazeGenerator.generate(regionRootX, regionRootZ, world.getSeed());
            MazeMapMemoryStorage.addMap(regionRoot, mazeMap);
        }
        else {
            mazeMap = storedGrid;
        }

        if(chunkX <= 0) {
            mapCoordinateX = Math.abs(chunkX % ArcanaMazeGenerator.MAZE_SIZE);
        }
        else {
            mapCoordinateX = ArcanaMazeGenerator.MAZE_SIZE - (chunkX % ArcanaMazeGenerator.MAZE_SIZE);
            if(mapCoordinateX == ArcanaMazeGenerator.MAZE_SIZE) { //bit messy but it works
                mapCoordinateX = 0;
            }
        }
        if(chunkZ <= 0) {
            mapCoordinateZ = Math.abs(chunkZ % ArcanaMazeGenerator.MAZE_SIZE);
        }
        else {
            mapCoordinateZ = ArcanaMazeGenerator.MAZE_SIZE - (chunkZ % ArcanaMazeGenerator.MAZE_SIZE);
            if(mapCoordinateZ == ArcanaMazeGenerator.MAZE_SIZE) {
                mapCoordinateZ = 0;
            }
        }

        Cell cell = mazeMap[mapCoordinateZ][mapCoordinateX]; //z has to come first because arrays are backwards from Cartesian plane logic
        ArcanaStructureHandler toGenerate = null;

        boolean debugMazeGen = false; //temporary, switch to true to use test pieces

        ArcanaStructureHandler crossroads = StructureRegistry.CROSSROADS_GEN_TEST;
        ArcanaStructureHandler junction = StructureRegistry.JUNCTION_GEN_TEST;
        ArcanaStructureHandler corner = StructureRegistry.CORNER_GEN_TEST;
        ArcanaStructureHandler hallway = StructureRegistry.HALLWAY_GEN_TEST;
        ArcanaStructureHandler deadEnd = StructureRegistry.DEAD_END_GEN_TEST;

        if(debugMazeGen) {
            crossroads = StructureRegistry.CROSSROADS_TEST;
            junction = StructureRegistry.JUNCTION_TEST;
            corner = StructureRegistry.CORNER_TEST;
            hallway = StructureRegistry.HALLWAY_TEST;
            deadEnd = StructureRegistry.DEAD_END_TEST;
        }

        switch(cell.getPieceType()) {
            case CROSSROADS:
                toGenerate = crossroads;
                break;
            case THREE_WAY:
                toGenerate = junction;
                break;
            case CORNER:
                toGenerate = corner;
                break;
            case HALLWAY:
                toGenerate = hallway;
                break;
            case DEAD_END:
                toGenerate = deadEnd;
                break;
        }

        Rotation rotation = cell.getRotation();
        toGenerate.generateWithRotation(this.world, this.rand, new BlockPos(x + 8, 8, z + 8), rotation);

        //biome.decorate(this.world, this.rand, pos);

        WorldEntitySpawner.performWorldGenSpawning(this.world, biome, x + 8, z + 8, 16, 16, this.rand);
    }

    private static int roundUpToMultiple(int numToRound, int multiple)
    {
        if (multiple == 0) {
            return numToRound;
        }

        int remainder = Math.abs(numToRound) % multiple;
        if (remainder == 0) {
            return numToRound;
        }

        if (numToRound < 0) {
            return -1 * (Math.abs(numToRound) - remainder);
        }
        else {
            return numToRound + multiple - remainder;
        }
    }
}