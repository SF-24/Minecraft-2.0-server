package net.mineshaft.structure.gen;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.mineshaft.structure.LootTableList;

import java.util.List;
import java.util.Random;

public class TowerGen {

    public static int generateFloor(World worldIn, Random rand, int i, int j, int k, int size, int floorHeight, boolean enforceStairs, boolean addStairHole, boolean raiseWindows) {
        return generateFloor(worldIn, rand, i, j, k, size, floorHeight, enforceStairs, addStairHole, raiseWindows, false, LootTableList.LootNether.NETHER_TOWER_GENERIC,false);
    }

    public static int generateFloor(World worldIn, Random rand, int i, int j, int k, int size, int floorHeight, boolean raiseWindows) {
        return generateFloor(worldIn,rand,i,j,k,size, floorHeight, false,false,raiseWindows, false, LootTableList.LootNether.NETHER_TOWER_GENERIC,false);
    }

    public static int generateFloor(World worldIn, Random rand, int i, int j, int k, int size, int floorHeight, boolean enforceStairs, boolean addStairHole, boolean raiseWindows, boolean addBrazier, List<WeightedRandomChestContent> lootTable) {
        return generateFloor(worldIn,rand,i,j,k,size, floorHeight, enforceStairs,addStairHole,raiseWindows, addBrazier, LootTableList.LootNether.NETHER_TOWER_GENERIC,false);
    }

    // Returns the height at which the next floor will begin to start.
    public static int generateFloor(World worldIn, Random rand, int i, int j, int k, int size, int floorHeight, boolean enforceStairs, boolean addStairHole, boolean raiseWindows, boolean addBrazier, List<WeightedRandomChestContent> lootTable, boolean raiseMobs) {
        if(floorHeight<7&&enforceStairs) {
            floorHeight = 7;
        } else if(floorHeight>8&&enforceStairs) {
            floorHeight = 8;
        }
        if(enforceStairs&& floorHeight==7&&size<4) size=4;
        if(enforceStairs&& floorHeight==8&&size<5) size=5;

        for(int x = -size; x<=size; x++) {
            for(int z = -size; z<=size; z++) {
                for(int y = -1; y<floorHeight; y++) {
                    if((x==-size || x==size || z==size || z==-size || y==-1)) {
                        if((x==-size || x==size) && (z==-size || z==size)) {
                            worldIn.setBlockPrimitive(x+i,y+j,z+k, Blocks.blackstone);
                        } else {
                            worldIn.setBlockPrimitive(x+i,y+j,z+k, Blocks.nether_brick);
                        }
                    } else {
                        worldIn.setBlockPrimitive(x+i,y+j,z+k, Blocks.air);
                    }
                }
            }
        }

        // Place the window
        placeWindow(worldIn,i+size,j+(raiseWindows?1:0),k);
        placeWindow(worldIn,i-size,j+(raiseWindows?1:0),k);
        placeWindow(worldIn,i,j+(raiseWindows?1:0),k+size);
        placeWindow(worldIn,i,j+(raiseWindows?1:0),k-size);

        // Add the stairs
        if(enforceStairs) {
            if(floorHeight==7) {
                worldIn.setBlockPrimitive(i-size+1,j,k-2, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+1,k-1, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+2,k, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+3,k+1, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+4,k+2, Blocks.nether_brick);
            } else if(floorHeight==8) {
                worldIn.setBlockPrimitive(i-size+1,j,k-3, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+1,k-2, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+2,k-1, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+3,k, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+4,k+1, Blocks.nether_brick);
                worldIn.setBlockPrimitive(i-size+1,j+5,k+2, Blocks.nether_brick);
            }
        }
        if(addStairHole) {
            worldIn.setBlockPrimitive(i-size+1,j-1,k-2, Blocks.air);
            worldIn.setBlockPrimitive(i-size+1,j-1,k-1, Blocks.air);
            worldIn.setBlockPrimitive(i-size+1,j-1,k, Blocks.air);
            worldIn.setBlockPrimitive(i-size+1,j-1,k+1, Blocks.air);
            worldIn.setBlockPrimitive(i-size+1,j-1,k+2, Blocks.air);
        }

        if(addBrazier) {
            worldIn.setBlockPrimitive(i,j,k,Blocks.netherrack);
            worldIn.setBlockPrimitive(i,j+1,k,Blocks.fire);
        }

        // Spawn the chest in a random corner
        BlockPos chestPos = new BlockPos(i+(size-1)*(rand.nextBoolean() ? 1 : -1),j,k+(size-1)*(rand.nextBoolean() ? 1 : -1));
        worldIn.setBlockState(chestPos, Blocks.chest.getDefaultState());
        TileEntity tileEntityChest = worldIn.getTileEntity(chestPos);
        if (tileEntityChest instanceof TileEntityChest) {
            WeightedRandomChestContent.generateChestContents(rand, lootTable, (TileEntityChest) tileEntityChest, LootTableList.LootNether.getNetherTowerLootCount(rand));
        }

        // Spawn guard mobs
        int mobCount = 3 + rand.nextInt(2);
        for (int iteration = 0; iteration < mobCount; iteration++) {
            EntityLiving guard;

            // 50% chance for a Zombie, 50% chance for a Skeleton
            if (rand.nextBoolean()) {
                guard = new EntityPigZombie(worldIn);
            } else {
                guard = new EntitySkeleton(worldIn);
            }

            guard.setLocationAndAngles(i,raiseMobs?j+2:j,k, rand.nextFloat() * 360.0F, 0.0F);

            // Initialize mob equipment and attributes (like weapon holding)
            guard.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(guard)), null);

            guard.enablePersistence();

            if(guard instanceof EntitySkeleton) {
                ((EntitySkeleton)guard).setSkeletonType(0);
                if(rand.nextBoolean()) {
                    guard.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
                    guard.setCurrentItemOrArmor(4, new ItemStack(Items.leather_helmet));
                } else {
                    guard.setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
                    guard.setCurrentItemOrArmor(3, new ItemStack(Items.iron_chestplate));
                    guard.setCurrentItemOrArmor(4, new ItemStack(Items.iron_helmet));
                }
            }
            for(int it = 0; it<5; it++) guard.setEquipmentDropChance(it,0);

            // Spawn the entity into the world
            worldIn.spawnEntityInWorld(guard);
        }

        return floorHeight+j-1;
    }

    // Returns the height at which the next floor will begin to start.
    public static void generateBasementFloor(World worldIn, Random rand, int i, int j, int k, int size, int floorHeight, boolean enforceStairs, boolean addStairHole, boolean raiseWindows, boolean addBrazier, List<WeightedRandomChestContent> lootTable) {
        generateFloor(worldIn,rand,i,j,k,size,floorHeight,enforceStairs,addStairHole,raiseWindows,addBrazier,lootTable,true);

        // Place the window
        placeWindow(worldIn,i+size,j+(raiseWindows?1:0),k,Blocks.nether_brick);
        placeWindow(worldIn,i-size,j+(raiseWindows?1:0),k,Blocks.nether_brick);
        placeWindow(worldIn,i,j+(raiseWindows?1:0),k+size,Blocks.nether_brick);
        placeWindow(worldIn,i,j+(raiseWindows?1:0),k-size,Blocks.nether_brick);

        for(int x = -1; x<=1; x++) {
            for(int z = -1; z<=1; z++) {
                if((z==-1 || z==1) && (x==-1 || x==1)) {
                    if(rand.nextInt(3)!=0) {
                        worldIn.setBlockPrimitive(x+i,j,z+k,rand.nextBoolean()?Blocks.nether_ash_ore:Blocks.netherrack);
                    }
                } else {
                    worldIn.setBlockPrimitive(x+i,j,z+k,Blocks.nether_ash_ore);
                    if(z==0 && x==0) {
                        worldIn.setBlockPrimitive(x+i,j+1,z+k,Blocks.nether_ash_ore);
                    } else if(rand.nextInt(4)!=0) {
                        worldIn.setBlockPrimitive(x+i,j+1,z+k,rand.nextBoolean()?Blocks.nether_ash_ore:Blocks.netherrack);
                    }
                }
            }
        }
    }

    public static void placeWindow(World world, int k, int j, int l) {
        placeWindow(world,k,j,l,Blocks.air);
    }

    public static void placeWindow(World world, int k, int j, int l, Block block) {
        world.setBlockPrimitive(k,j,l,block);
        world.setBlockPrimitive(k,j+1,l,block);
    }

    public static void placeLadder(World world, int k, int j, int l, int height) {
        for(int h = 0; h<height; h++) {
            world.setBlockPrimitive(k,j+h,l,Blocks.ladder);
        }
    }

    // Later, I will add a random roof
    public static void generateRoof(World worldIn, Random rand, int k, int j, int l, int size) {
        boolean isCharred = rand.nextBoolean();
        if(rand.nextBoolean()) {
            for(int x = -size-1; x<=size+1; x++) {
                for(int z = -size-1; z<=size+1; z++) {
                    if(isCharred) {
                        worldIn.setBlockPrimitive(x+k,j,z+l, Blocks.blackstone);
                    } else {
                        worldIn.setBlockPrimitive(x+k,j,z+l, Blocks.nether_brick);
                    }
                }
            }
        } else {
            for(int x = -size; x<=size; x++) {
                for(int z = -size; z<=size; z++) {
                    if(isCharred) {
                        worldIn.setBlockPrimitive(x+k,j,z+l, Blocks.blackstone);
                    } else {
                        if(x==size||z==size||x==-size||z==-size) {
                            worldIn.setBlockPrimitive(x+k,j,z+l, Blocks.blackstone);
                        } else {
                            worldIn.setBlockPrimitive(x+k,j,z+l, Blocks.nether_brick);
                        }
                    }
                }
            }
        }
    }
}
