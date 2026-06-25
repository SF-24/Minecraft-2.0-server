package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BiomeGenAether extends BiomeGenBase
{
    private WorldGenerator theWorldGenerator = new WorldGenMinable(Blocks.monster_egg.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.STONE), 9);
    private WorldGenTreesAnySurface trees = new WorldGenTreesAnySurface(false);

    public BiomeGenAether(int id) {
        super(id);
        this.hasBeach=false;
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySheep.class, 10, 1, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySlime.class, 10, 4, 4));
        this.topBlock = Blocks.grass.getDefaultState();
        this.fillerBlock = Blocks.dirt.getDefaultState();
        this.theBiomeDecorator = new BiomeAetherDecorator();
    }

    public WorldGenAbstractTree genBigTreeChance(Random rand)
    {
        return rand.nextInt(2) > 0 ? this.worldGeneratorTrees : super.genBigTreeChance(rand);
    }

    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos)
    {
        super.decorate(worldIn, rand, pos);
        genWells(worldIn,rand,pos);
    }

    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
    {
        super.genTerrainBlocks(worldIn,rand,chunkPrimerIn,x,z,noiseVal);
        this.topBlock = Blocks.grass.getDefaultState();
        this.fillerBlock = Blocks.dirt.getDefaultState();
        this.generateBiomeTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    protected BiomeGenBase createMutatedBiome(int p_180277_1_)
    {
        return (new BiomeGenAether(p_180277_1_));
    }


    public void genWells(World worldIn, Random rand, BlockPos pos) {
        if (rand.nextInt(100/*was 10*/) == 0) {
            int i = rand.nextInt(16) + 8;
            int j = rand.nextInt(16) + 8;
            BlockPos blockpos = worldIn.getHeight(pos.add(i, 0, j)).up();

            final List<WeightedRandomChestContent> chestContents = Lists.newArrayList(
                    new WeightedRandomChestContent(Items.glowing_bread, 0, 1, 1, 2),
                    new WeightedRandomChestContent(Items.glowstone_dust, 0, 1, 6, 30),
                    new WeightedRandomChestContent(Items.slime_ball, 0, 1, 2, 5),
//                    new WeightedRandomChestContent(Items.ruby, 0, 1, 1, 2),
                    new WeightedRandomChestContent(Items.apple, 0, 1, 2, 10),
                    new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 2),
                    new WeightedRandomChestContent(Items.record_magnetic_circuit, 0, 1, 1, 1),
                    new WeightedRandomChestContent(Items.bread, 0, 1, 2, 5),
                    new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 2, 5),
                    new WeightedRandomChestContent(Items.gold_nugget, 0, 2, 10, 5));

            BlockPos p = worldIn.getPrecipitationHeight(blockpos.add(i, 0, j)).up();
            // Gen the stuff
            if((new WorldGenAetherPillar()).generateBlocks(worldIn, rand, p.down())) {
                worldIn.setBlockState(p.down(2), Blocks.chest.getDefaultState());
                TileEntity tileentity1 = worldIn.getTileEntity(p.down(2));
                if (tileentity1 instanceof TileEntityChest) {
                    WeightedRandomChestContent.generateChestContents(rand, chestContents, (TileEntityChest) tileentity1, 8);
                }
            }
        }
    }
}
