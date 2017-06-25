package leshainc.mechanical.common.block;

import leshainc.mechanical.Mechanical;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("deprecation")
public class BlockBeltConveyor extends Block /* implements ITileEntityProvider */ {
    public static String NAME = "belt_conveyor";

    private static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final PropertyBool FRONT = PropertyBool.create("front");
    private static final PropertyBool BACK = PropertyBool.create("back");
    private static final PropertyBool LEFT = PropertyBool.create("left");
    private static final PropertyBool RIGHT = PropertyBool.create("right");

    BlockBeltConveyor() {
        super(Material.IRON);
        setUnlocalizedName(Mechanical.NAME + "." + BlockBeltConveyor.NAME);
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
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, FRONT, BACK, LEFT, RIGHT);
    }

//    @Nullable
//    @Override
//    public TileEntity createNewTileEntity(World worldIn, int meta) {
//        return new TileEntityBeltConveyor();
//    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
                                EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()), 2);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        worldIn.setBlockState(pos, state.withProperty(FRONT, false), 2);
        return true;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos ownPos) {
        EnumFacing facing = state.getValue(FACING);
        return state
                .withProperty(FRONT, canConnectTo(state, world.getBlockState(ownPos.offset(facing))))
                .withProperty(BACK, canConnectTo(state, world.getBlockState(ownPos.offset(facing.getOpposite()))))
                .withProperty(LEFT, canConnectTo(state, world.getBlockState(ownPos.offset(facing.rotateYCCW()))))
                .withProperty(RIGHT, canConnectTo(state, world.getBlockState(ownPos.offset(facing.rotateY()))));

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return new AxisAlignedBB(0,0,0,1,.5,1);
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

    private boolean canConnectTo(IBlockState ownState, IBlockState neighborState) {
        Block neighborBlock = neighborState.getBlock();
        if (!(neighborBlock instanceof BlockBeltConveyor))
            return false;

//        EnumFacing ownFacing = ownState.getValue(FACING);
//        EnumFacing neighborFacing = neighborState.getValue(FACING);
//
//        return !ownFacing.equals(neighborFacing.getOpposite());
        return true;
    }
}
