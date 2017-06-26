package leshainc.mechanical.common.block;

import leshainc.mechanical.Mechanical;
import leshainc.mechanical.util.EnumLocalFacing;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        worldIn.setBlockState(pos, state.withProperty(FRONT, false), 2);
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
