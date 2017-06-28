package leshainc.mechanical.common.block;

import leshainc.mechanical.Mechanical;
import leshainc.mechanical.common.tileentity.TileEntityBeltConveyor;
import leshainc.mechanical.util.AABBHelper;
import leshainc.mechanical.util.EnumLocalFacing;
import leshainc.mechanical.util.ItemHandlerHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("deprecation")
public class BlockBeltConveyor extends Block implements ITileEntityProvider {
    public static String NAME = "belt_conveyor";

    private static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final PropertyBool FRONT = PropertyBool.create("front");
    private static final PropertyBool BACK = PropertyBool.create("back");
    private static final PropertyBool LEFT = PropertyBool.create("left");
    private static final PropertyBool RIGHT = PropertyBool.create("right");

    private static final AxisAlignedBB AABB_MIDDLE = new AxisAlignedBB(
            0.171875, 0, 0.015625,
            0.828125, 0.515625, 0.984375);

    // TODO: get correct coordinates:
    private static final AxisAlignedBB AABB_FRONT = new AxisAlignedBB(
            0.171875, 0, 0.015625,
            0.828125, 0.515625, 0.984375);
    private static final AxisAlignedBB AABB_BACK = new AxisAlignedBB(
            0.171875, 0, 0.015625,
            0.828125, 0.515625, 0.984375);
    private static final AxisAlignedBB AABB_LEFT = new AxisAlignedBB(
            0.171875, 0, 0.015625,
            0.828125, 0.515625, 0.984375);
    private static final AxisAlignedBB AABB_RIGHT = new AxisAlignedBB(
            0.171875, 0, 0.015625,
            0.828125, 0.515625, 0.984375);

    BlockBeltConveyor() {
        super(Material.IRON);
        setUnlocalizedName(Mechanical.MODID + "." + BlockBeltConveyor.NAME);
        setRegistryName(BlockBeltConveyor.NAME);
        setDefaultState(
                this.blockState.getBaseState()
                        .withProperty(FACING, EnumFacing.NORTH)
                        .withProperty(FRONT, false)
                        .withProperty(BACK, false)
                        .withProperty(LEFT, false)
                        .withProperty(RIGHT, false));

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityBeltConveyor te = (TileEntityBeltConveyor) worldIn.getTileEntity(pos);
        assert(te != null);

        ItemHandlerHelper.dropItemHandlerItems(worldIn, pos, te.inventory);

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, FRONT, BACK, LEFT, RIGHT);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBeltConveyor();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
                                EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()), 2);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
            return true;

        TileEntityBeltConveyor te = (TileEntityBeltConveyor) worldIn.getTileEntity(pos);
        assert(te != null);  // TODO: Remove assert

        ItemStack heldItem = playerIn.getHeldItem(hand);

        if (!heldItem.isEmpty()) {
            playerIn.setHeldItem(hand, ItemHandlerHelper.insertItem(te.inventory, heldItem, false));
        } else{
            for (int slot = 0; slot < te.inventory.getSlots(); slot++) {
                Mechanical.log.info(te.inventory.getStackInSlot(slot).toString());
            }
        }

        return true;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos ownPos) {
        EnumFacing facing = state.getValue(FACING);

        boolean front = canConnectTo(state, world.getBlockState(ownPos.offset(facing)), EnumLocalFacing.FRONT);
        boolean back = canConnectTo(state, world.getBlockState(ownPos.offset(facing.getOpposite())), EnumLocalFacing.BACK);
        boolean left = canConnectTo(state, world.getBlockState(ownPos.offset(facing.rotateYCCW())), EnumLocalFacing.LEFT);
        boolean right = canConnectTo(state, world.getBlockState(ownPos.offset(facing.rotateY())), EnumLocalFacing.RIGHT);

        return state
                .withProperty(FRONT, front)
                .withProperty(BACK, back)
                .withProperty(LEFT, left)
                .withProperty(RIGHT, right);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABBHelper.rotateY(AABB_MIDDLE, state.getValue(FACING).getHorizontalIndex());
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
                                      List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn,
                                      boolean p_185477_7_ /* TODO: figure out what is this */)
    {
        int angle = state.getValue(FACING).getHorizontalIndex();
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBHelper.rotateY(AABB_MIDDLE, angle));

        if (state.getValue(FRONT)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBHelper.rotateY(AABB_FRONT, angle));
        }

        if (state.getValue(BACK)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBHelper.rotateY(AABB_BACK, angle));
        }

        if (state.getValue(LEFT)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBHelper.rotateY(AABB_LEFT, angle));
        }

        if (state.getValue(RIGHT)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABBHelper.rotateY(AABB_RIGHT, angle));
        }
    }

    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    private boolean canConnectTo(IBlockState ownState, IBlockState neighborState, EnumLocalFacing localFacing) {
        Block neighborBlock = neighborState.getBlock();
        if (!(neighborBlock instanceof BlockBeltConveyor))
            return false;

        EnumFacing ownFacing = ownState.getValue(FACING);
        EnumFacing neighborFacing = neighborState.getValue(FACING);

        switch (localFacing) {
            case FRONT:
                return !ownFacing.equals(neighborFacing.getOpposite());
            case BACK:
                return ownFacing.equals(neighborFacing);
            case LEFT:
                return ownFacing.equals(neighborFacing.rotateYCCW());
            case RIGHT:
                return ownFacing.equals(neighborFacing.rotateY());
            default:
                return false;
        }
    }
}
