package net.fabricmc.example;
import net.minecraft.block.Block;

import java.lang.Math;
import java.util.Deque;
import java.util.ArrayDeque;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.UseAction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MovementType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import org.lwjgl.glfw.GLFW;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.GenericContainerScreenHandler;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.fabricmc.api.ClientModInitializer;

public class ExampleClientMod implements ClientModInitializer {
		public enum State {
			PLANT2_REPLENISH,
			PLANT2_REPLENISH_HOTBAR,
			PLANT2_REPLENISH_MAIN,
			PLANT2_NEXT,
			REPLENISH,
			REPLENISH_HOTBAR,
			REPLENISH_MAIN,
			WATER2_START,
			WATER2_SKIP_BLOCK,
			WATER2,
			WATER2_PICK,
			WATER2_PICK_CLICK,
			WATER2_PLACE,
			WATER2_PLACE_LEFT,
			WATER2_PLACE_CLICK,
			WATER2_PLACE_LEFT_CLICK,
			WATER2_REVERSE,
			WATER2_REVERSE_PICK,
			WATER2_REVERSE_PICK_CLICK,
			WATER2_REVERSE_PLACE_CLICK,
			WATER2_REVERSE_PLACE_LEFT_CLICK,
			WATER2_REVERSE_PLACE,
			WATER2_REVERSE_PLACE_LEFT,
			WATER2_END_LANE,
			WATER2_FIND_LANE,
			WATER2_NEW_LANE,
			WATER2_CENTER_BLOCK,
			WATER,
			WATER_LEFT,
			WATER_RIGHT,
			WATER_RIGHT_CLICK,
			GOTO_BLOCK_CENTER,
			INITIALIZE,
			FARM,
			INVENTORY_FULL,
			SLEEP,
			FIND_LANE,
			DUMP_TO_CHEST,
			BACK_TO_START,
			CRAFT,
			CRAFT_ENCHANTED_SUGAR,
			CLICK_CRAFTING_TABLE,
			BUILD,
			BUILD_CHECK,
			BUILD_END,
			PLANT,
			PLANT2,
			PLANT2_DOIT,
			PLANT2_PLACE,
			BRIDGE_V,
			BRIDGE_V_HALL,
			BRIDGE_V_END_LANE,
			BRIDGE_V_FIND_SPOT,
			BRIDGE_V_DOWN,
			BRIDGE_V_LEFT,
			BRIDGE_V_RIGHT,
			BRIDGE_V_CHECK,
			BRIDGE_V_START_LANE_LEFT,
			BRIDGE_V_START_LANE_LEFT_CLICK,
			BRIDGE_V_START_LANE_RIGHT,
			BRIDGE_V_START_LANE_RIGHT_CLICK,
			BRIDGE_N,
			BRIDGE_N_DOWN,
			BRIDGE_N_CHECK,
			BRIDGE_N_FIND_SPOT,

			BRIDGE_N_CLICK,
			BRIDGE_N_DOWN_CLICK,
			BRIDGE_N_CHECK_CLICK,
			BRIDGE_N_FIND_SPOT_CLICK,

			BRIDGING,
			BRIDGE,
			BRIDGE_FIND_SPOT,
			BRIDGE_END,
			BRIDGE_CHECK,
			BRIDGE_CHECK_END,
			BRIDGE_DOWN,
			BRIDGE_DOWN_CLICK,
			BRIDGE_LEFT,
			BRIDGE_RIGHT,


			BRIDGE_M,
			BRIDGE_M_FIND_SPOT,
			BRIDGE_M_END,
			BRIDGE_M_CHECK,
			BRIDGE_M_CHECK_END,
			BRIDGE_M_DOWN,
			BRIDGE_M_DOWN_CLICK,
			BRIDGE_M_LEFT,
			BRIDGE_M_RIGHT,


			BRIDGE_V_START_LANE,
			BRIDGE_V_START_LANE_WALK_LEFT,
			BRIDGE_V_START_LANE_PLACE_BOTTOM_LEFT,
			BRIDGE_V_START_LANE_PLACE_LEFT,
			BRIDGE_V_START_LANE_WALK_RIGHT,
			BRIDGE_V_START_LANE_PLACE_BOTTOM_RIGHT,
			BRIDGE_V_START_LANE_PLACE_RIGHT,
			BRIDGE_V_START_LANE_WALK_LEFT_CHECK,
			BRIDGE_V_START_LANE_WALK_RIGHT_WALL,
			BRIDGE_V_START_LANE_WALK_RIGHT_WALL_CHECK,
			BRIDGE_V_START_LANE_WALK_RIGHT_WALL_LEFT_WALK,
			BRIDGE_V_NEW_LANE,



			BRIDGE_V_START_LANE_CLICK,
			BRIDGE_V_START_LANE_WALK_LEFT_CLICK,
			BRIDGE_V_START_LANE_PLACE_BOTTOM_LEFT_CLICK,
			BRIDGE_V_START_LANE_PLACE_LEFT_CLICK,
			BRIDGE_V_START_LANE_WALK_RIGHT_CLICK,
			BRIDGE_V_START_LANE_PLACE_BOTTOM_RIGHT_CLICK,
			BRIDGE_V_START_LANE_PLACE_RIGHT_CLICK,
			BRIDGE_V_START_LANE_WALK_LEFT_CHECK_CLICK,
			BRIDGE_V_START_LANE_WALK_RIGHT_WALL_CLICK,
			BRIDGE_V_START_LANE_WALK_RIGHT_WALL_CHECK_CLICK,
			BRIDGE_V_START_LANE_WALK_RIGHT_WALL_LEFT_WALK_CLICK,
			BRIDGE_V_NEW_LANE_CLICK,

			BRIDGE_V_CLICK,
			BRIDGE_V_HALL_CLICK,
			BRIDGE_V_END_LANE_CLICK,
			BRIDGE_V_FIND_SPOT_CLICK,
			BRIDGE_V_DOWN_CLICK,
			BRIDGE_V_LEFT_CLICK,
			BRIDGE_V_RIGHT_CLICK,
			BRIDGE_V_CHECK_CLICK,
		}
	public class MyBot {
		private State state;
		private final int  WATER_TICKS = 5;
		private boolean water2_lane_end = false;
		private int water2lane = 5;
		private Direction hallTraversing;
		private int plant2_walk = 0;
		private int plant_place;
		private boolean water_placed = false;
		private Item findItem = Items.DIRT;
		private Direction facing;
		private boolean replenished = false;
		private boolean out_of_stock = false;
		private boolean return_state = false;
		public MinecraftClient client;
		private ClientPlayerEntity player;
		private Direction traversing;
		private float defYaw;
		private float defPitch;
		private double prevZ;
		private double prevX;
		private double prevY;
		private float dYaw;
		private boolean midDump;
		private int startSlot;
		private int endSlot;
		private int emptySlot;
		private int delayTick;
		private int skipTicks;
		private boolean rightLaneTraversal;
		private int ingredients;
		private boolean justSwitched;
		private int enchanted_sugar;
		private State buildState;
		private State bridgeState;
		private int buildCount;
		private static final int MAXBUILD = 1026;
		private static final int MAX_TICK_DELAY = 8;
		private final double MAX_TICK_DISTANCE = 5;
		private BlockPos placedBlockPos;
		private BlockPos curBlockPos;
		private BlockPos prevBlockPos;
		private Vec3d prevPos;
		private final double EDGE_DELTA_MAX = 0.3;
		private final float PITCH_DELTA = 10F;
		private final int ESUGAR_INGREDIENTS = 5;
		private final int EPAPER_INGREDIENTS = 6;
		private final int MAX_INGREDIENTS = ESUGAR_INGREDIENTS;  // 6 for enchanted paper, 5 for enchanted sugar
		private final int HYPIXEL_CRAFT_RESULT_SLOT = 23;
		private boolean justBorn = true;
		private boolean born = false;
		private Vec3d lastPos;
		private BlockPos lastBlockPos;
		private int sugarCount;
		private float prevYaw;
		private State prevBridgeState;
		private State prevState;
		private State nextState;
		private boolean bridgeSuccess;
		private Vec3d target;
		private BlockPos targetBlockPos;
		private State bridgeVState;
		private Direction hallFace;
		private Direction laneFace;
		private boolean breakingBlock;
		private int bridgeN;
		private int bridgeM;
		private int bridgeNFind;
		private State callerState;
		private State callingState;
		private Deque<State> callerStates;
		private int emptyHotbarSlot;



		public boolean isCloseToBlockCenter()
		{
			return this.player.getPos().distanceTo(Vec3d.ofBottomCenter(this.player.getBlockPos())) < 0.3;
		}

		public MyBot() {
			state = State.INITIALIZE;
		}
		public boolean water2Lane()
		{
			if (this.water2lane-- > 0)
				return false;
			this.water2lane = 5;
			return true;
		}
		public void faceLeft()
		{
			this.player.setYaw(this.getFacing().rotateYCounterclockwise().asRotation());
		}
		public void faceRight()
		{
			this.player.setYaw(this.getFacing().rotateYClockwise().asRotation());
		}
		public void faceBack()
		{
			this.player.setYaw(this.getFacing().rotateYCounterclockwise().rotateYCounterclockwise().asRotation());
		}
		public void moveTowardsX(Vec3d pos)
		{
			double me = this.player.getX();
			double her = pos.getX();
			Direction facing = this.getFacing();
			if (me > her) {
				switch (facing) {
				case NORTH :
					this.moveLeft();
					break;
				case SOUTH :
					this.moveRight();
					break;
				case EAST :
					this.moveBack();
					break;
				case WEST :
					this.moveForward();
					break;
				default :
				break;
				}
			} else if (me < her) {
				switch (facing) {
				case NORTH :
					this.moveRight();
					break;
				case SOUTH :
					this.moveLeft();
					break;
				case EAST :
					this.moveForward();
					break;
				case WEST :
					this.moveBack();
					break;
				default :
				break;
				}
			}
		}
		public void moveTowardsZ(Vec3d pos)
		{
			double me = this.player.getZ();
			double her = pos.getZ();
			Direction facing = this.getFacing();
			if (me > her) {
				switch (facing) {
				case NORTH :
					this.moveForward();
					break;
				case SOUTH :
					this.moveBack();
					break;
				case EAST :
					this.moveLeft();
					break;
				case WEST :
					this.moveRight();
					break;
				default :
				break;
				}
			} else if (me < her) {
				switch (facing) {
				case NORTH :
					this.moveBack();
					break;
				case SOUTH :
					this.moveForward();
					break;
				case EAST :
					this.moveRight();
					break;
				case WEST :
					this.moveLeft();
					break;
				default :
				break;
				}
			}
		}

		public Direction getFacing()
		{
			return Direction.fromRotation(this.player.getYaw());
		}
		public double getSide(Vec3d pos)
		{
			return pos.getComponentAlongAxis(this.getFacing().rotateYClockwise().getAxis());
		}
		public double getDepth(Vec3d pos)
		{
			return pos.getComponentAlongAxis(this.getFacing().getAxis());
		}
		public void callState(State calledState)
		{
			this.callerStates.addFirst(this.state);
			this.callerState = this.state;
			this.changeState(calledState);
		}
		public void retState()
		{
			this.prevState = this.callerStates.peekFirst();
			this.changeState(this.callerStates.removeFirst());
		}
		public State getPrevState()
		{
			return this.prevState;
		}
		public void skipTicks(int ticks)
		{
			this.skipTicks = ticks;
		}
		public void addSkipTicks(int ticks)
		{
			this.skipTicks += ticks;
		}
		public boolean skipTick()
		{
			if (this.skipTicks > 0) {
				this.skipTicks--;
				return true;
			}
			return false;
		}
		public void setDelay(int delayTicks)
		{
			this.delayTick = delayTicks;
		}
		public void doDelay()
		{
			this.setDelay(MAX_TICK_DELAY);
		}
		public boolean isDelay()
		{
			if (this.delayTick > 0) {
				delayTick--;
				return true;
			}
			return false;
		}
		// calls subroutine
		public void callSub(State sub, State next)
		{
			this.nextState = next;
			this.changeState(sub);

		}
		public void retSub()
		{
			this.changeState(this.nextState);
		}
		public void changeState(State nextState)
		{
			this.prevState = this.state;
			this.state = nextState;
		}
		public void backState()
		{
			State tmp = this.state;
			this.state = this.prevState;
			this.prevState = tmp;
		}
		public void sameState()
		{
			this.prevState = this.state;
		}
		// points 0.25 around y center
		public Vec3d getBlockFaceRandom(BlockPos pos, Direction facing)
		{
			Vec3d ret = Vec3d.ofCenter(pos);
			// the random delta
			double randelta = -0.1 + (0.2 * Math.random());
			switch (facing) {
			case NORTH :
				ret = ret.add(0, randelta, 0.5);
				break;
			case SOUTH :
				ret = ret.add(0, randelta, -0.5);
				break;
			case EAST :
				ret = ret.add(-0.5, randelta, 0);
				break;
			case WEST :
				ret = ret.add(0.5, randelta, 0);
				break;
			case UP :
				ret = ret.add(randelta, -0.5, 0);
				break;
			case DOWN :
				ret = ret.add(randelta, 0.5, 0);
				break;
			default :
				break;
			}
			return ret;
		}

		public Vec3d getBlockFaceRand(BlockPos pos, Direction facing)
		{
			return this.getBlockFaceRandom(pos, facing.getOpposite());

		}
		public Vec3d getBlockFaceRandom(BlockPos pos)
		{
			Vec3d ret = Vec3d.ofCenter(pos);
			// the random delta
			double randelta = -0.1 + (0.2 * Math.random());
			switch (this.player.getHorizontalFacing()) {
			case NORTH :
				ret = ret.add(0, randelta, 0.5);
				break;
			case SOUTH :
				ret = ret.add(0, randelta, -0.5);
				break;
			case EAST :
				ret = ret.add(-0.5, randelta, 0);
				break;
			case WEST :
				ret = ret.add(0.5, randelta, 0);
				break;
			default :
				break;
			}
			return ret;
		}
		public Vec3d getBlockFace(BlockPos pos)
		{
			Vec3d ret = Vec3d.ofCenter(pos);
			if (this.isFacingEast()) {
				ret = ret.add(-0.5, 0, 0);
			} else if (this.isFacingWest()) {
				ret = ret.add(0.5, 0, 0);
			} else if (this.isFacingNorth()) {
				ret = ret.add(0, 0, 0.5);
			} else if (this.isFacingSouth()) {
				ret = ret.add(0, 0, -0.5);
			}
			return ret;
		}
		public void lookAt(double lookX, double lookY, double lookZ)
		{
			this.player.setPitch(this.getTargetPitch(lookX, lookY, lookZ));
			this.player.setYaw(this.getTargetYaw(lookX, lookZ));
		}
		public void lookAtRand(Vec3d pos)
		{
			double rand = -0.1 + (0.2 * Math.random());
			this.lookAt(pos.getX() + rand, pos.getY() + rand, pos.getZ() + rand);
		}
		public void lookAtRandXZ(Vec3d pos)
		{
			double rand = -0.1 + (0.2 * Math.random());
			this.lookAt(pos.getX() + rand, pos.getY(), pos.getZ() + rand);
		}
		public void lookAt(Vec3d pos)
		{
			this.lookAt(pos.getX(), pos.getY(), pos.getZ());
		}
		public void lookAt(Vec3i pos)
		{
			this.lookAt(Vec3d.of(pos));
		}


		private float getTargetPitch(double lookX, double lookY, double lookZ) {
      			double d = lookX - this.player.getX();
      			double e = lookY - this.player.getEyeY();
      			double f = lookZ - this.player.getZ();
      			double g = Math.sqrt(d * d + f * f);
      			return (float)(-(MathHelper.atan2(e, g) * 57.2957763671875D));
   		}

		private float getTargetYaw(double lookX, double lookZ) {
      			double d = lookX - this.player.getX();
      			double e = lookZ - this.player.getZ();
      			return (float)(MathHelper.atan2(e, d) * 57.2957763671875D) - 90.0F;
   		}

		public State build() {
			this.stop();
			if (buildState == null) {
				buildState = State.BUILD;
				buildCount = MAXBUILD;
			}
			State retState = State.BUILD;
			switch (buildState) {
			case BUILD :
				this.stop();
				this.player.setPitch(90);
//				LOGGER.info("player.setYaw(" + this.player.getYaw() + " isFacingEast() = " + this.isFacingEast()));
				this.straightYaw();
//				LOGGER.info("player.setYaw(" + this.player.getYaw() + " isFacingEast() = " + this.isFacingEast()));
				/*
				this.player.setYaw(90);
				double deltaX = Math.abs(this.player.getX() - (int)this.player.getX());
				if (deltaX > 0.5)
					deltaX = 1 - deltaX;
				*/
				double delta = this.getBlockEdgeDelta();
				this.player.setPitch((float)(90 - (PITCH_DELTA *(delta / EDGE_DELTA_MAX))));
//				LOGGER.info("delta =" + delta);
//				LOGGER.info("buildCount " + buildCount);
				// attempt to place block
				if (!isDownBlockSolid() && delta < EDGE_DELTA_MAX ) {
					this.rightClick(); this.player.swingHand(Hand.MAIN_HAND);
					placedBlockPos = new BlockPos(this.player.getX(),this.player.getY() - 1,this.player.getZ());
					buildState = State.BUILD_CHECK;
					delayTick = 0;
				//	this.moveForward(); // to counteract the existing velocity
				//	this.sprint();
					//this.sneak();
					LOGGER.info("before build check x " + this.player.getX() + " y " + this.player.getY() + " z " + this.player.getZ());
				} else { //just keep backing up until we find a spot
					this.moveBack();
				}
				break;
			case BUILD_CHECK :
				this.stop();
				if (delayTick > 0) {
					delayTick--;
					break;
				}
				LOGGER.info("after build check x " + this.player.getX() + " y " + this.player.getY() + " z " + this.player.getZ());
//				LOGGER.info("checking if block placement successful");
				//boolean blockPlaced = this.getAxisDelta() > 0.5 ? this.isDownFrontBlockSolid() : this.isDownBlockSolid();
				//if (blockPlaced && buildCount > 0 && this.player.getInventory().getMainHandStack() != ItemStack.EMPTY) {
				if (this.isBlockPosSolid(placedBlockPos) && buildCount > 0 && this.player.getInventory().getMainHandStack() != ItemStack.EMPTY && !this.isBackBlockSolid()) {
					buildCount--;
					//this.moveBack();
					this.buildState = State.BUILD;
				} else {
					LOGGER.info("block placement failed: isblockpos solid " + this.isBlockPosSolid(placedBlockPos));
					LOGGER.info("axis delta " + this.getAxisDelta());
					LOGGER.info("blockedge delta " + this.getBlockEdgeDelta());
					LOGGER.info("blockpos x = " + placedBlockPos.getX() + " y = " + placedBlockPos.getY() + " z = " + placedBlockPos.getZ());
					LOGGER.info("player x =" + this.player.getX() + " y =" + this.player.getY() + " z = " + this.player.getZ());
					LOGGER.info("buildCount " + buildCount);
					LOGGER.info("mainhandstack count" + this.player.getInventory().getMainHandStack().getCount());
					LOGGER.info("getPitch() " + this.player.getPitch());
					LOGGER.info("getYaw() " + this.player.getYaw());
					this.buildState = State.BUILD_END;
					this.stop();
					this.moveForward();
				}
				/*
				LOGGER.info("checking if block placement successful");
				boolean blockPlaced = this.getAxisDelta() > 0.5 ? this.isDownFrontBlockSolid() : this.isDownBlockSolid();
				if (blockPlaced && buildCount > 0 && this.player.getInventory().getMainHandStack() != ItemStack.EMPTY) {
					buildCount--;
					this.moveBack();
					this.buildState = State.BUILD;
				} else {
					LOGGER.info("axis delta " + this.getAxisDelta());
					this.buildState = State.BUILD_END;
					this.stop();
				}
				*/

				break;
			case BUILD_END :
				this.stop();
				LOGGER.info("isblockpos solid " + this.isBlockPosSolid(placedBlockPos));
				LOGGER.info("blockpos x = " + placedBlockPos.getX() + " y = " + placedBlockPos.getY() + " z = " + placedBlockPos.getZ());
				LOGGER.info("done building");
				buildCount = MAXBUILD;
				retState = State.SLEEP;
				buildState = State.BUILD;
				break;
			default :
				break;
			}
			return retState;
		}
		public void straightFace(Direction facing)
		{
			this.player.setYaw(facing.asRotation());
		}
		public void straightYaw()
		{
			if (this.isFacingEast())
				this.player.setYaw(-90);
			else if (this.isFacingWest())
				this.player.setYaw(90);
			else if (this.isFacingNorth())
				this.player.setYaw(180);
			else if (this.isFacingSouth())
				this.player.setYaw(0);
		}
		// how close are we from the front or back block's edge
		public double getBlockEdgeDelta()
		{
			double delta;
			if (this.isFacingEast() || this.isFacingWest())
				delta = Math.abs(this.player.getX() - (int)this.player.getX());
			else
				delta = Math.abs(this.player.getZ() - (int)this.player.getZ());

			if (delta > 0.5)
				delta = 1 - delta;
			return delta;
		}
		public double getAxisDelta()
		{
			double delta;
			if (this.isFacingEast() || this.isFacingWest())
				delta = Math.abs(this.player.getX() - (int)this.player.getX());
			else
				delta = Math.abs(this.player.getZ() - (int)this.player.getZ());
			return delta;
		}


		//Blocks blks = new Blocks;
		public void tick() {
//			LOGGER.info("tick() state: " + this.state);
			if (this.state != State.SLEEP)
				this.stop();
			if (this.skipTick())
				return;
			switch(state) {
			case INITIALIZE :
				// do initialize stuff
				break;
			case FIND_LANE :
				LOGGER.info("finding next lane");
				this.stop();
				this.moveBack();
				BlockPos nowBlockPos = this.player.getBlockPos();
				// travelled more than 2 blocks
				if (nowBlockPos.getSquaredDistance(lastBlockPos) > 4
					&& this.isSugarFace()  // is facing a non-solid block
					&& this.player.getPos().distanceTo(Vec3d.ofBottomCenter(this.player.getBlockPos())) < 0.3) { // is close to center
					this.state = State.FARM;
					this.rightLaneTraversal = !this.rightLaneTraversal;
					break;
				}
				LOGGER.info("squared distance" + nowBlockPos.getSquaredDistance(lastBlockPos) + " sugarface " + this.isSugarFace() + " distance to center " + this.player.getPos().distanceTo(Vec3d.ofBottomCenter(this.player.getBlockPos())));
				if (this.rightLaneTraversal)  {
					this.moveRight(); 
					if (this.isRightBlockSolid()) {
						this.state = State.BACK_TO_START;
						break;
					}
				} else {
					this.moveLeft();
					if (this.isLeftBlockSolid()) {
						this.state = State.BACK_TO_START;
						break;
					}
				}


				break;
			case BACK_TO_START :
				LOGGER.info("going back to start");
				this.stop();
				if (this.rightLaneTraversal)
					this.moveLeft();
				else
					this.moveRight();
				if ((this.rightLaneTraversal && this.isLeftBlockSolid())
					|| (!this.rightLaneTraversal && this.isRightBlockSolid())) {
					this.stop();
					this.rightLaneTraversal = !this.rightLaneTraversal;
					this.state = State.FARM;
				}
				break;
			case BUILD :
				this.state = this.build();
				break;

			case BRIDGE_M :
				if (this.prevState == State.BRIDGE_M_FIND_SPOT) {
					this.retState();
					break;
				}
				this.stop();
				this.straightYaw();
				this.prevYaw = this.player.getYaw();
				this.changeState(State.BRIDGE_M_FIND_SPOT);
				break;
			case BRIDGE_M_FIND_SPOT :
				if (this.bridgeM <= 0) {
					this.changeState(State.BRIDGE_M);
					break;
				}
				this.stop();
				this.player.setYaw(this.prevYaw);
				this.curBlockPos = this.player.getBlockPos();
				this.printPos("bridge_find_spot", this.player.getPos());
				if (this.prevBlockPos == null)
					this.prevBlockPos = this.curBlockPos;
				if (!this.isDownBlockSolid()) {
					if (this.curBlockPos != this.prevBlockPos) {
						if (this.getBlockEdgeDelta() < 0.2) {
							LOGGER.info("found good spot");
							this.stop();
							this.sneak();
							this.moveBack();
							this.changeState(State.BRIDGE_M_DOWN);
						} else {
							this.moveBack(); // too far off the edge
							this.sameState();
						}
					} else {
						// block placement failed, try again
						this.moveBack();
						this.sameState();
					}
				} else
				if (this.isFrontBlockSolid()) {
					LOGGER.info("path blocked");
					this.stop();
					this.changeState(State.SLEEP);
				} else {	
					this.moveForward();
					this.sameState();
				}
				this.prevBlockPos = this.curBlockPos;
				break;
			case BRIDGE_M_DOWN :
				this.stop();
				if (isDelay())
					break;
				this.player.setYaw(this.prevYaw);
				this.target = this.getBlockFaceRandom(this.downBackBlockPos(), Direction.fromRotation(this.prevYaw).getOpposite());
				this.printPos("target", this.target);
				this.lookAt(target);
				this.addSkipTicks(MAX_TICK_DELAY);
				this.changeState(State.BRIDGE_M_DOWN_CLICK);
				break;
			case BRIDGE_M_DOWN_CLICK :
				//LOGGER.info("BRIDGE_M_DOWN rightclicked");
				this.rightClick();
				this.placedBlockPos = this.downBlockPos();
				this.changeState(State.BRIDGE_M_CHECK);
				break;
			case BRIDGE_M_CHECK :
				this.stop();
				LOGGER.info("prevState " + this.prevState + " this.state " + this.state);
				ItemStack itemStack1 = this.player.getInventory().getMainHandStack();
				if (!this.isBlockPosSolid(this.placedBlockPos)) {
					if (itemStack1 == ItemStack.EMPTY) {
						LOGGER.info("empty stack");
						// find a block item
						int found = -1;
						for (int i = 0; i < this.player.getInventory().getHotbarSize(); i++) {
//							if (this.player.getInventory().getStack(i).getUseAction() == UseAction.BLOCK) {
							if (this.player.getInventory().getStack(i).getItem().getGroup() == ItemGroup.BUILDING_BLOCKS) {
								found = i;
								break;
							}
						}
						if (found >= 0) {
						//	this.player.networkHandler.sendPacket(new PickFromInventoryC2SPacket(found));
							this.player.getInventory().selectedSlot = found; 
							this.backState();
							this.setDelay(5);
						} else {
							LOGGER.info("prevState " + this.prevState + " this.state " + this.state);
							LOGGER.info("no blocks found in inventory");
							this.changeState(State.SLEEP);
						}
					} else {
						LOGGER.info("block placement failed");
						LOGGER.info("placed block x" + this.placedBlockPos.getX() +  " y " + this.placedBlockPos.getY() + " z "  + this.placedBlockPos.getZ());
						LOGGER.info("player getYaw()" + this.player.getYaw() + "player getPitch()" + this.player.getPitch());
						LOGGER.info("player x" + this.player.getX() + "player y" + this.player.getY() + " player z " + this.player.getZ());
						this.printPos("target", this.target);
	//					this.state = this.prevState; // try again
						this.changeState(State.SLEEP);
						this.stop();
					}
				} else {
					//LOGGER.info("success");
					this.changeState(State.BRIDGE_M_FIND_SPOT);
					this.bridgeM--;
				}
				break;

			case BRIDGE :
				this.stop();
				this.straightYaw();
				this.prevYaw = this.player.getYaw();
				this.changeState(State.BRIDGE_FIND_SPOT);
				break;
			case BRIDGE_FIND_SPOT :
				this.stop();
				this.player.setYaw(this.prevYaw);
				this.curBlockPos = this.player.getBlockPos();
				this.printPos("bridge_find_spot", this.player.getPos());
				if (this.prevBlockPos == null)
					this.prevBlockPos = this.curBlockPos;
				if (!this.isDownBlockSolid()) {
					if (this.curBlockPos != this.prevBlockPos) {
						if (this.getBlockEdgeDelta() < 0.2) {
							LOGGER.info("found good spot");
							this.stop();
							this.sneak();
							this.moveBack();
							this.changeState(State.BRIDGE_DOWN);
						} else {
							this.moveBack(); // too far off the edge
							this.sameState();
						}
					} else {
						// block placement failed, try again
						this.moveBack();
						this.sameState();
					}
				} else
				if (this.isFrontBlockSolid()) {
					LOGGER.info("path blocked");
					this.stop();
					this.changeState(State.SLEEP);
				} else {	
					this.moveForward();
					this.sameState();
				}
				this.prevBlockPos = this.curBlockPos;
				break;
			case BRIDGE_DOWN :
				this.stop();
				if (isDelay())
					break;
				this.player.setYaw(this.prevYaw);
				this.target = this.getBlockFaceRandom(this.downBackBlockPos(), Direction.fromRotation(this.prevYaw).getOpposite());
				this.printPos("target", this.target);
				this.lookAt(target);
				this.addSkipTicks(MAX_TICK_DELAY);
				this.changeState(State.BRIDGE_DOWN_CLICK);
				break;
			case BRIDGE_DOWN_CLICK :
				//LOGGER.info("BRIDGE_DOWN rightclicked");
				this.rightClick();
				this.placedBlockPos = this.downBlockPos();
				this.changeState(State.BRIDGE_CHECK);
				break;
			case BRIDGE_CHECK :
				this.stop();
				LOGGER.info("prevState " + this.prevState + " this.state " + this.state);
				ItemStack itemStack4 = this.player.getInventory().getMainHandStack();
				if (!this.isBlockPosSolid(this.placedBlockPos)) {
					if (itemStack4 == ItemStack.EMPTY) {
						LOGGER.info("empty stack");
						// find a block item
						int found = -1;
						for (int i = 0; i < this.player.getInventory().getHotbarSize(); i++) {
//							if (this.player.getInventory().getStack(i).getUseAction() == UseAction.BLOCK) {
							if (this.player.getInventory().getStack(i).getItem().getGroup() == ItemGroup.BUILDING_BLOCKS) {
								found = i;
								break;
							}
						}
						if (found >= 0) {
						//	this.player.networkHandler.sendPacket(new PickFromInventoryC2SPacket(found));
							this.player.getInventory().selectedSlot = found; 
							this.backState();
							this.setDelay(5);
						} else {
							LOGGER.info("prevState " + this.prevState + " this.state " + this.state);
							LOGGER.info("no blocks found in inventory");
							this.changeState(State.SLEEP);
						}
					} else {
						LOGGER.info("block placement failed");
						LOGGER.info("placed block x" + this.placedBlockPos.getX() +  " y " + this.placedBlockPos.getY() + " z "  + this.placedBlockPos.getZ());
						LOGGER.info("player getYaw()" + this.player.getYaw() + "player getPitch()" + this.player.getPitch());
						LOGGER.info("player x" + this.player.getX() + "player y" + this.player.getY() + " player z " + this.player.getZ());
						this.printPos("target", this.target);
	//					this.state = this.prevState; // try again
						this.changeState(State.SLEEP);
						this.stop();
					}
				} else {
					//LOGGER.info("success");
					this.changeState(State.BRIDGE_FIND_SPOT);
				}
				break;
			case BRIDGE_V :
				this.stop();
				this.straightYaw();
				this.prevYaw = this.player.getYaw();
				this.hallFace = Direction.fromRotation(this.player.getYaw()).rotateYCounterclockwise();
				this.laneFace = Direction.fromRotation(this.player.getYaw());
				this.changeState(State.BRIDGE_V_FIND_SPOT);
				break;
			case BRIDGE_V_FIND_SPOT :
				this.stop();
				this.player.setYaw(this.prevYaw);
				this.curBlockPos = this.player.getBlockPos();
				this.printPos("bridge_v_find_spot", this.player.getPos());
				if (this.prevBlockPos == null)
					this.prevBlockPos = this.curBlockPos;
				if (!this.isDownBlockSolid()) {
					if (this.curBlockPos != this.prevBlockPos) {
						if (this.getBlockEdgeDelta() < 0.25) {
							LOGGER.info("found good spot");
							this.stop();
							this.sneak();
							this.moveBack();
							this.changeState(State.BRIDGE_V_DOWN);
						} else {
							this.moveBack(); // too far off the edge
							this.sameState();
						}
					} else {
						// block placement failed, try again
						this.moveBack();
						this.sameState();
					}
				} else
				if (this.isFrontBlockSolid()) {
					LOGGER.info("path blocked");
					this.stop();
					this.changeState(State.BRIDGE_V_END_LANE);
				} else {	
					this.moveForward();
					this.sameState();
				}
				this.prevBlockPos = this.curBlockPos;
				break;
			case BRIDGE_V_END_LANE :
				LOGGER.info("BRIDGE_V_END_LANE");
				this.stop();
				this.player.setYaw(this.hallFace.asRotation());
				this.changeState(State.BRIDGE_V_HALL);
				this.breakingBlock = false;
				break;
			case BRIDGE_V_HALL :
				LOGGER.info("BRIDGE_V_HALL");
				this.stop();
				if (this.prevState == State.BRIDGE_V_END_LANE) {
					this.callState(State.GOTO_BLOCK_CENTER);
					break;
				}
				if (this.isDelay())
					break;
				if (this.isFrontBlockSolid()) {
					LOGGER.info("breaking block");
					if (!breakingBlock) {
						this.printPos("target block", this.frontBlockPos());
						LOGGER.info("block face" + this.player.getHorizontalFacing().getOpposite());
						this.leftClick();
					}
					this.changeState(this.state);
				//	this.setDelay(5);
					break;
				} else  
				if (this.prevState != State.BRIDGE_M) {
					LOGGER.info("making hall");
					this.bridgeM = 3;
					this.callState(State.BRIDGE_M);
					this.breakingBlock = false;
					break;
				}
				LOGGER.info("hall made, starting new lane");
				this.laneFace = this.laneFace.getOpposite();
				this.player.setYaw(this.laneFace.asRotation());
				this.changeState(State.BRIDGE_V_START_LANE);
				break;
			case BRIDGE_V_START_LANE :
				this.stop();
				this.straightFace(this.laneFace);
				/*
				if (this.prevState == State.BRIDGE_V_HALL) {
					this.bridgeN = 1;
					this.callState(State.BRIDGE_N);
					break;
				}
				*/
				if (this.prevState != State.GOTO_BLOCK_CENTER) {
					this.callState(State.GOTO_BLOCK_CENTER);
					break;
				}
				//this.callState(State.BRIDGE_V_START_LANE_WALK_LEFT);
				this.changeState(State.BRIDGE_V_START_LANE_LEFT);
				this.prevBlockPos = this.player.getBlockPos();
				break;
			case BRIDGE_V_START_LANE_LEFT :
				this.lookAt(this.getBlockFaceRand(this.getRelativeBlockPos(this.laneFace, -1, 0, 1), this.laneFace));
				this.addSkipTicks(5);
				this.changeState(State.BRIDGE_V_START_LANE_LEFT_CLICK);
				break;
			case BRIDGE_V_START_LANE_LEFT_CLICK :
				this.rightClick();
				this.changeState(State.BRIDGE_V_START_LANE_RIGHT);
				break;
			case BRIDGE_V_START_LANE_RIGHT :
				this.lookAt(this.getBlockFaceRand(this.getRelativeBlockPos(this.laneFace, 1, 0, 1), this.laneFace));
				this.addSkipTicks(5);
				this.changeState(State.BRIDGE_V_START_LANE_RIGHT_CLICK);
				break;
			case BRIDGE_V_START_LANE_RIGHT_CLICK :
				this.rightClick();
				this.changeState(State.BRIDGE_V_NEW_LANE);
				break;
			case BRIDGE_V_START_LANE_WALK_LEFT :
				this.stop();
				this.straightFace(this.laneFace);
				if (this.prevState == State.BRIDGE_V_START_LANE) {
					this.callState(State.GOTO_BLOCK_CENTER);
					break;
				}
				this.printPos("walking left", this.player.getPos());
				if (this.prevBlockPos == this.player.getBlockPos()) {
					this.moveLeft();
					this.sameState();
					break;
				}
				this.stop();
				this.sneak();
				this.prevBlockPos = this.player.getBlockPos();
				this.target = this.getBlockFaceRand(this.downRightBlockPos() , this.laneFace.rotateYCounterclockwise()); 
				this.lookAt(this.target);
				this.changeState(State.BRIDGE_V_START_LANE_WALK_LEFT_CLICK);
				this.addSkipTicks(MAX_TICK_DELAY);
				break;
			case BRIDGE_V_START_LANE_WALK_LEFT_CLICK :
				this.rightClick();
				this.changeState(State.BRIDGE_V_START_LANE_WALK_LEFT_CHECK);
				LOGGER.info("right clicked");
				this.printPos("target", getBlockFaceRandom(this.downRightBlockPos()));
				LOGGER.info("getYaw()" + this.player.getYaw());
				break;
			case BRIDGE_V_START_LANE_WALK_LEFT_CHECK :
				this.stop();
				this.sneak();
				LOGGER.info("left_check");
				//this.player.setYaw(this.laneFace.asRotation());
				if (this.isDownBlockSolid()) {
					this.prevBlockPos = this.player.getBlockPos();
					this.changeState(State.BRIDGE_V_START_LANE_WALK_RIGHT);
				} else {
					LOGGER.info("lane face" + this.laneFace);
					LOGGER.info("lane face counterclockwise" + this.laneFace.rotateYCounterclockwise());
					LOGGER.info("block placement failed");
					this.printPos("target pos", this.target);
					this.printPos("my pos", this.player.getPos());
					LOGGER.info("my getYaw()" + this.player.getYaw());
					LOGGER.info("my getPitch()" + this.player.getPitch());
					this.changeState(State.SLEEP);
				//	this.changeState(State.BRIDGE_V_START_LANE_WALK_LEFT);
				}
				break;
			case BRIDGE_V_START_LANE_WALK_RIGHT :
				this.straightFace(this.laneFace);
				this.printPos("walking right", this.player.getPos());
				this.stop();
				if (this.prevBlockPos == this.player.getBlockPos()) {
					this.moveRight();
					this.sameState();
					break;
				}
				if (this.prevState == State.BRIDGE_V_START_LANE_WALK_RIGHT) {
					this.callState(State.GOTO_BLOCK_CENTER);
					break;
				}
				this.straightFace(this.laneFace);
				this.target =this.getBlockFaceRand(this.downLeftBlockPos() , Direction.UP); 
				this.lookAt(this.getBlockFaceRand(this.downLeftBlockPos() , Direction.UP));
				this.changeState(State.BRIDGE_V_START_LANE_WALK_RIGHT_CLICK);
				this.addSkipTicks(MAX_TICK_DELAY);
				break;
			case BRIDGE_V_START_LANE_WALK_RIGHT_CLICK:
				this.rightClick();
				LOGGER.info("right clicked");
				this.printPos("WALK_RIGHT target", this.target);
			//	this.changeState(State.BRIDGE_V_START_LANE_WALK_RIGHT_WALL);
				this.changeState(State.BRIDGE_V_START_LANE_WALK_RIGHT_WALL);
				this.prevBlockPos = this.player.getBlockPos();
				break;
			case BRIDGE_V_START_LANE_WALK_RIGHT_WALL :
				this.printPos("making right wall", this.player.getPos());
				this.stop();
				this.straightFace(this.laneFace);
				/*
				if (this.prevState == State.BRIDGE_V_START_LANE) {
					this.callState(State.GOTO_BLOCK_CENTER);
					break;
				}
				*/
				if (this.prevBlockPos == this.player.getBlockPos()) {
					this.moveRight();
					this.sameState();
					break;
				}
				this.stop();
				this.sneak();
				this.prevBlockPos = this.player.getBlockPos();
				this.target = this.getBlockFaceRand(this.downLeftBlockPos() , this.laneFace.rotateYClockwise()); 
				this.lookAt(this.target);
				this.addSkipTicks(MAX_TICK_DELAY);
				this.changeState(State.BRIDGE_V_START_LANE_WALK_RIGHT_WALL_CLICK);
				break;
			case BRIDGE_V_START_LANE_WALK_RIGHT_WALL_CLICK :
				this.rightClick();
				this.changeState(State.BRIDGE_V_START_LANE_WALK_RIGHT_WALL_CHECK);
				LOGGER.info("right clicked");
				this.printPos("target", getBlockFaceRandom(this.downRightBlockPos()));
				LOGGER.info("getYaw()" + this.player.getYaw());
				break;
			case BRIDGE_V_START_LANE_WALK_RIGHT_WALL_CHECK :
				this.stop();
				this.sneak();
				LOGGER.info("left_check");
				//this.player.setYaw(this.laneFace.asRotation());
				if (this.isDownBlockSolid()) {
					this.prevBlockPos = this.player.getBlockPos();
					this.changeState(State.BRIDGE_V_START_LANE_WALK_RIGHT_WALL_LEFT_WALK);
				} else {
					LOGGER.info("lane face" + this.laneFace);
					LOGGER.info("lane face counterclockwise" + this.laneFace.rotateYCounterclockwise());
					LOGGER.info("block placement failed");
					this.printPos("target pos", this.target);
					this.printPos("my pos", this.player.getPos());
					LOGGER.info("my getYaw()" + this.player.getYaw());
					LOGGER.info("my getPitch()" + this.player.getPitch());
					this.changeState(State.SLEEP);
				//	this.changeState(State.BRIDGE_V_START_LANE_WALK_LEFT);
				}
				break;
			case BRIDGE_V_START_LANE_WALK_RIGHT_WALL_LEFT_WALK :
				LOGGER.info("RIGHT_WALL_LEFT_WALK");
				this.straightFace(this.laneFace);
				this.printPos("walking right", this.player.getPos());
				this.stop();
				if (this.prevBlockPos == this.player.getBlockPos()) {
					this.moveLeft();
					this.sameState();
					break;
				}
				if (this.prevState == State.BRIDGE_V_START_LANE_WALK_RIGHT_WALL_LEFT_WALK) {
					this.callState(State.GOTO_BLOCK_CENTER);
					break;
				}
				this.lookAt(this.getBlockFaceRandom(this.downRightBlockPos() , Direction.UP));
				this.changeState(State.BRIDGE_V_START_LANE_WALK_RIGHT_WALL_LEFT_WALK);
				this.addSkipTicks(MAX_TICK_DELAY);
				break;
			case BRIDGE_V_START_LANE_WALK_RIGHT_WALL_LEFT_WALK_CLICK :
				this.rightClick();
				LOGGER.info("right clicked");
				this.changeState(State.BRIDGE_V_NEW_LANE);
				break;
			case BRIDGE_V_NEW_LANE :
				this.stop();
				this.straightFace(this.laneFace);
				this.prevYaw = this.player.getYaw();
				this.prevBlockPos = this.player.getBlockPos();
				this.changeState(State.BRIDGE_V_FIND_SPOT);
				break;
			case BRIDGE_V_DOWN :
				this.stop();
				if (isDelay())
					break;
				this.player.setYaw(this.prevYaw);
				this.target = this.getBlockFaceRandom(this.downBackBlockPos(), Direction.fromRotation(this.prevYaw).getOpposite());
				this.printPos("target", this.target);
				this.lookAt(target);
				this.changeState(State.BRIDGE_V_DOWN_CLICK);
				this.addSkipTicks(MAX_TICK_DELAY);
				//LOGGER.info("BRIDGE_V_DOWN rightclicked");
				break;
			case BRIDGE_V_DOWN_CLICK :
				this.rightClick();
				this.placedBlockPos = this.downBlockPos();
				this.changeState(State.BRIDGE_V_CHECK);
				break;
			case BRIDGE_V_LEFT :
				this.stop();
				if (isDelay())
					break;
				this.player.setYaw(this.prevYaw);
				this.placedBlockPos = this.leftBlockPos();
				this.target = this.getBlockFaceRandom(this.backLeftBlockPos(), Direction.fromRotation(this.prevYaw).getOpposite());
				this.printPos("target", this.target);
				this.lookAt(target);
				this.changeState(State.BRIDGE_V_LEFT_CLICK);
				this.addSkipTicks(MAX_TICK_DELAY);
				break;
				//LOGGER.info("BRIDGE_V_DOWN rightclicked");
			case BRIDGE_V_LEFT_CLICK :
				this.rightClick();
				LOGGER.info("BRIDGE_V_LEFT placedBlockPos ", this.placedBlockPos);
				this.changeState(State.BRIDGE_V_CHECK);
				break;
			case BRIDGE_V_RIGHT :
				this.stop();
				if (isDelay())
					break;
				this.player.setYaw(this.prevYaw);
				this.placedBlockPos = this.rightBlockPos();
				this.target = this.getBlockFaceRandom(this.backRightBlockPos(), Direction.fromRotation(this.prevYaw).getOpposite());
				this.printPos("target", this.target);
				this.lookAt(target);
				this.changeState(State.BRIDGE_V_RIGHT_CLICK);
				this.addSkipTicks(MAX_TICK_DELAY);
				break;
				//LOGGER.info("BRIDGE_V_DOWN rightclicked");
				//this.rightClick();
			case BRIDGE_V_RIGHT_CLICK:
				this.rightClick();
				this.changeState(State.BRIDGE_V_CHECK);
				break;
			case BRIDGE_V_CHECK :
				this.stop();
				LOGGER.info("prevState " + this.prevState + " this.state " + this.state);
				ItemStack itemStack2 = this.player.getInventory().getMainHandStack();
				if (this.replenished || this.prevState == State.REPLENISH) {
					if (itemStack2 != ItemStack.EMPTY) {
						LOGGER.info("BRIDGE_V_CHECK: just replenished, backstating");
						this.prevState = this.callingState;
						this.backState();
					} else {
						LOGGER.info("BRIDGE_V_CHECK: completely out of stock");
						// completely out of stock
						this.changeState(State.SLEEP);
					}
					this.replenished = false;
					break;
				}
				if (!this.isBlockPosSolid(this.placedBlockPos)) {
					if (itemStack2 == ItemStack.EMPTY) {
						LOGGER.info("empty stack");
						// find a block item
						int found = -1;
						for (int i = 0; i < this.player.getInventory().getHotbarSize(); i++) {
//							if (this.player.getInventory().getStack(i).getUseAction() == UseAction.BLOCK) {
							if (this.player.getInventory().getStack(i).getItem().getGroup() == ItemGroup.BUILDING_BLOCKS) {
								found = i;
								break;
							}
						}
						if (found >= 0) {
							LOGGER.info("found block");
				//			this.player.networkHandler.sendPacket(new PickFromInventoryC2SPacket(found));
							this.player.getInventory().selectedSlot = found; 
							this.backState();
						//	this.setDelay(5);
							this.skipTicks(5);
						} else {
							LOGGER.info("prevState " + this.prevState + " this.state " + this.state);
							LOGGER.info("no blocks found in inventory");
							this.findItem = Items.GRASS_BLOCK;
							this.callingState = this.prevState;
							this.callState(State.REPLENISH);
						}
					} else {
						LOGGER.info("block placement failed");
						LOGGER.info("placed block x" + this.placedBlockPos.getX() +  " y " + this.placedBlockPos.getY() + " z "  + this.placedBlockPos.getZ());
						LOGGER.info("player getYaw()" + this.player.getYaw() + "player getPitch()" + this.player.getPitch());
						LOGGER.info("player x" + this.player.getX() + "player y" + this.player.getY() + " player z " + this.player.getZ());
						this.printPos("target", this.target);
	//					this.state = this.prevState; // try again
						this.changeState(State.SLEEP);
						this.stop();
					}
				} else {
					//LOGGER.info("success");
					switch (this.prevState) {
					case BRIDGE_V_DOWN_CLICK :
						this.changeState(State.BRIDGE_V_RIGHT);
						break;
					case BRIDGE_V_LEFT_CLICK :
						this.changeState(State.BRIDGE_V_FIND_SPOT);
						break;
					case BRIDGE_V_RIGHT_CLICK :
						this.changeState(State.BRIDGE_V_LEFT);
						break;
					default :
						break;
					}
					this.setDelay(5);
				}
				break;
			case BRIDGE_N :
				LOGGER.info("bridgeN: " + this.bridgeN + " callerState " + this.callerState + "callerStates[0] " + this.callerStates.peekFirst());
				if (this.prevState == State.BRIDGE_N_FIND_SPOT) {
					this.retState();
					break;
				}
				//this.callerState = this.prevState;
				this.straightYaw();
				this.prevYaw = this.player.getYaw();
				this.changeState(State.BRIDGE_N_FIND_SPOT);
				this.bridgeNFind = 0;
				this.prevPos = this.player.getPos();
				break;
			case BRIDGE_N_FIND_SPOT :
				this.player.setYaw(this.prevYaw);
				if (this.bridgeN <= 0) {
					this.changeState(State.BRIDGE_N);
					break;
				}
				this.curBlockPos = this.player.getBlockPos();
				this.printPos("bridge_find_spot", this.player.getPos());
				if (this.prevBlockPos == null)
					this.prevBlockPos = this.curBlockPos;
				//haven't moved blocks, try again
				if (this.prevBlockPos == this.curBlockPos) {	
					LOGGER.info("have not found, moving foraward");
					if (this.prevState == State.BRIDGE_N_FIND_SPOT && this.bridgeNFind > 0) {
						// not moving at all
						this.printPos("BRIDGE_N_FIND_SPOT my pos: ", this.player.getPos());
						if (this.player.getPos().distanceTo(this.prevPos) == 0) {
							this.bridgeNFind = 0;
							this.callState(State.GOTO_BLOCK_CENTER);
							break;
						}
					}
					this.moveForward();
					this.sameState();
					this.bridgeNFind++;
					this.prevPos = this.player.getPos();
					break;
				}
				if (!this.isDownBlockSolid()) {
					if (this.curBlockPos != this.prevBlockPos) {
						if (this.getBlockEdgeDelta() < 0.2) {
							LOGGER.info("found good spot");
							this.stop();
							this.sneak();
							this.moveBack();
							this.changeState(State.BRIDGE_N_DOWN);
						} else {
							this.moveBack(); // too far off the edge
							this.sameState();
						}
					} else {
						// block placement failed, try again
						this.moveBack();
						this.sameState();
					}
				} else
				if (this.isFrontBlockSolid()) {
					LOGGER.info("path blocked");
					this.stop();
					this.changeState(State.SLEEP);
					this.bridgeNFind = 0;
				} else {	
					LOGGER.info("have not found, moving foraward");
					if (this.prevState == State.BRIDGE_N_FIND_SPOT && this.bridgeNFind > 0) {
						//not moving, possible blockage

						if (this.player.getPos().distanceTo(this.prevPos) == 0) {
							this.bridgeNFind = 0;
							this.callState(State.GOTO_BLOCK_CENTER);
							break;
						}
					}
					this.moveForward();
					this.sameState();
					this.bridgeNFind++;
				}
				this.prevPos = this.player.getPos();
				this.prevBlockPos = this.curBlockPos;
				break;
			case GOTO_BLOCK_CENTER :
				this.stop();
				Vec3d me = this.player.getPos();
				Vec3d center = Vec3d.ofBottomCenter(this.player.getBlockPos());
				if (me.isInRange(center, 0.25)) {
					this.retState();
				} else {
					this.moveTowardsZ(center);
					this.moveTowardsX(center);
					this.sameState();
				}
				break;
			case BRIDGE_N_DOWN :
				this.player.setYaw(this.prevYaw);
				this.target = this.getBlockFaceRandom(this.downBackBlockPos(), Direction.fromRotation(this.prevYaw).getOpposite());
				this.printPos("target", this.target); 
				this.lookAt(target); //LOGGER.info("BRIDGE_N_DOWN rightclicked");
				this.changeState(State.BRIDGE_N_DOWN_CLICK);
				this.addSkipTicks(MAX_TICK_DELAY);
				break;
			case BRIDGE_N_DOWN_CLICK :
				this.rightClick();
				this.placedBlockPos = this.downBlockPos();
				this.changeState(State.BRIDGE_N_CHECK);
				break;
			case BRIDGE_N_CHECK :
				LOGGER.info("BRIDGE_N_CHECK prevState " + this.prevState + " this.state " + this.state);
				ItemStack itemStack3 = this.player.getInventory().getMainHandStack();
				if (!this.isBlockPosSolid(this.placedBlockPos)) {
					if (itemStack3 == ItemStack.EMPTY) {
						LOGGER.info("BRIDGE_N_CHECK empty stack");
						// find a block item
						int found = -1;
						for (int i = 0; i < this.player.getInventory().size(); i++) {
//							if (this.player.getInventory().getStack(i).getUseAction() == UseAction.BLOCK) {
							if (this.player.getInventory().getStack(i).getItem().getGroup() == ItemGroup.BUILDING_BLOCKS) {
								found = i;
								break;
							}
						}
						if (found >= 0) {
							this.player.networkHandler.sendPacket(new PickFromInventoryC2SPacket(found));
							this.backState();
							this.skipTicks(5);
						} else {
							LOGGER.info("BRIDGE_N_CHECK prevState " + this.prevState + " this.state " + this.state);
							LOGGER.info("BRIDGE_N_CHECK no blocks found in inventory");
							this.changeState(State.SLEEP);
						}
					} else {
						LOGGER.info("BRIDGE_N_CHECK block placement failed");
						LOGGER.info("BRIDGE_N_CHECK placed block x" + this.placedBlockPos.getX() +  " y " + this.placedBlockPos.getY() + " z "  + this.placedBlockPos.getZ());
						LOGGER.info("BRIDGE_N_CHECK player getYaw()" + this.player.getYaw() + "player getPitch()" + this.player.getPitch());
						LOGGER.info("BRIDGE_N_CHECK player x" + this.player.getX() + "player y" + this.player.getY() + " player z " + this.player.getZ());
						this.printPos("BRIDGE_N_CHECK target", this.target);
	//					this.state = this.prevState; // try again
						this.changeState(State.SLEEP);
					}
				} else {
					//LOGGER.info("success");
					this.changeState(State.BRIDGE_N_FIND_SPOT);
					this.bridgeN--;
				}
				break;
			case FARM :
				// do next tick stuff
//				if (client.world.getBlockState(new BlockPos(player.getX() + 1, player.getY(), player.getZ())).getBlock().getClass().equals(blks.SUGAR_CANE.getclass()))
				this.stop();
				if (!isSugarFace()) {
					LOGGER.info("dead end, stop turn around and find next lane");
					this.stop();
					this.defYaw += isFacingEast() ? 180 : -180;
					this.defaultFace();
					this.lastBlockPos = this.player.getBlockPos();
					state = State.FIND_LANE;
					break;

				}
				if (client.player.getInventory().getEmptySlot() < 0) {
					LOGGER.info("inventory full");
					this.state = State.INVENTORY_FULL;
					break;
				}
//				LOGGER.info("head shaking");
				// shake head
				this.defaultFace();
				this.dYaw *= -1; //				this.player.getYaw() += this.dYaw;
				this.dYaw +=  0.1 * Math.random();

				this.player.setYaw(this.player.getYaw() + this.dYaw);
				this.attack();
				this.sprint();
				this.moveForward();
//				LOGGER.info("dYaw: " + this.dYaw);

				break;
			case INVENTORY_FULL : // dumping sugarcane stacks to chest
				//state = State.DUMP_TO_CHEST;
				
				this.stop();
				this.state = State.SLEEP;
				LOGGER.info("clicking menu");
				this.rightClickHotbar(8);
				this.state = State.CRAFT;
				this.skipTicks(7);
				break;
			case CRAFT :
				this.stop();
				LOGGER.info("crafting");
				if (this.client.currentScreen != null) {
					if (this.client.currentScreen.getTitle().getString().equals((String)"SkyBlock Menu")) {
						LOGGER.info("clicked skyblock menu");
						this.client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, 31, 0, SlotActionType.PICKUP, client.player);
						this.skipTicks(10);	
						this.state = State.CLICK_CRAFTING_TABLE;
						break;

					} else {
						LOGGER.info("screen title != SkyBlock Menu");
						this.player.closeHandledScreen();
						this.state = State.FARM;
					}
				} else {
					LOGGER.info("CRAFT: currentScreen == NULL");
					this.player.closeHandledScreen();
					this.state = State.FARM;
				}
				break;
				/*
			case WATER_TRANSFER :
				this.facing = this.getFacing();
				this.straightFace();
				this.traversing = this.facing;
				this.changeState(State.WATER_TRANSFER_DOIT);
				break;
			case WATER_TRANSFER_DOIT :
				this.stop();
				this.straightFace(this.facing);
				this.curBlockPos = this.player.getBlockPos();
				if (this.isDownFrontBlockSolid()) {
					this.retState();
					break;
				}
				if (this.prevBlockPos == null)
					this.prevBlockPos = this.curBlockPos;
				if (this.curBlockPos == this.prevBlockPos) {
					this.moveRight();
					this.sameState();
					break;
				}
				this.prevBlockPos = this.curBlockPos;
				this.lookAtRandXZ(Vec3d.ofBottomCenter(this.downRightBlockPos()));
				this.rightClick();
				this.changeState(State.WATER_TRANSFER_LEFT);
				break;
			case WATER_TRANSFER_LEFT :
				this.lookAtRandXZ(Vec3d.ofBottomCenter(this.downLeftBlockPos().offset(this.facing.rotateYCounterclockwise())));
				this.rightClick();
				break;
			*/

			case WATER2_START :
				this.hallTraversing = this.getFacing().rotateYCounterclockwise();
				this.changeState(State.WATER2);
				break;
			case WATER2 :
				/*
				if (this.player.getInventory().getMainHandStack() == ItemStack.EMPTY ||  this.isFrontBlockSolid()) {
					LOGGER.info("stack count" + this.player.getInventory().getMainHandStack().getCount());
					LOGGER.info("front block solid " + this.isFrontBlockSolid());
					this.stop();
					this.state = State.SLEEP;
					break;
				} else {
					this.sprint();
					this.moveForward();
				}
				*/
				if (this.getPrevState() == State.WATER2_PICK) {
					this.changeState(State.WATER2_END_LANE);
					break;
				}
				this.facing = this.getFacing();
				this.traversing = this.facing;
				this.straightFace(this.facing);
				this.changeState(State.WATER2_PICK);
				this.water_placed = false;
				this.prevBlockPos = this.player.getBlockPos();// skip first block
				break;
			case WATER2_END_LANE :
				this.facing = this.facing.getOpposite();
				this.traversing = this.traversing.getOpposite();
				this.straightFace(this.hallTraversing);
				this.moveForward();
				this.changeState(State.WATER2_FIND_LANE);
				this.prevBlockPos = this.player.getBlockPos();
				break;
			case WATER2_FIND_LANE :
				this.stop();
				this.curBlockPos = this.player.getBlockPos();
				if (this.prevBlockPos == null)
					this.prevBlockPos = this.curBlockPos;
				if (this.curBlockPos == this.prevBlockPos) {
					this.moveForward();
					this.sameState();
					break;
				}
				if (this.water2Lane()) {
					this.changeState(State.WATER2_CENTER_BLOCK);
					break;
				}
				this.prevBlockPos = this.curBlockPos;

				break;
			case WATER2_CENTER_BLOCK :
				this.stop();
				if (this.isCloseToBlockCenter()) {
					this.prevBlockPos = this.player.getBlockPos();
					this.changeState(State.WATER2_SKIP_BLOCK);
				} else {
					this.moveForward();
					this.sameState();
				}
				break;
			case WATER2_SKIP_BLOCK :
				// skip one block
				this.stop();
				this.straightFace(this.traversing);
				this.curBlockPos = this.player.getBlockPos();
				if (this.curBlockPos == this.prevBlockPos) {
					this.moveForward();
					this.sameState();
					break;
				}
				this.changeState(State.WATER2_NEW_LANE);
				break;
			case WATER2_NEW_LANE :
				if (this.traversing.rotateYCounterclockwise() == this.hallTraversing)
					this.changeState(State.WATER2);
				else
					this.changeState(State.WATER2_REVERSE);
				break;
			case WATER2_REVERSE :
				if (this.getPrevState() == State.WATER2_REVERSE_PICK) {
					this.changeState(State.WATER2_END_LANE);
					break;
				}
				this.facing = this.getFacing();
				this.traversing = this.facing;
				this.straightFace(this.facing);
				this.changeState(State.WATER2_REVERSE_PICK);
				this.water_placed = false;
				this.prevBlockPos = this.player.getBlockPos();// skip first block
				break;
			case WATER2_REVERSE_PICK:
				this.stop();
				this.straightFace(this.facing);
				this.curBlockPos = this.player.getBlockPos();
				if (this.isFrontBlockSolid()) {
					this.changeState(State.WATER2);
					break;
				}
				if (this.prevBlockPos == null)
					this.prevBlockPos = this.curBlockPos;
				if (this.curBlockPos == this.prevBlockPos && !this.water_placed) {
					this.moveForward();
					this.sameState();
					break;
				}
				this.prevBlockPos = this.curBlockPos;
				//this.targetBlockPos = this.downLeftBlockPos().offset(this.facing.rotateYClockwise(), 2);
				this.targetBlockPos = this.getRelativeBlockPos(this.traversing, -2, -1, 0);
				this.printPos("WATER2_REVERSE_PICK", this.targetBlockPos);
				//this.target = Vec3d.ofBottomCenter(this.targetBlockPos);
				this.target = this.getBlockFaceRand(this.targetBlockPos, Direction.UP);
				//this.printPos("WATER2_REVERSE_RIGHT target: ", this.target);
				this.lookAtRandXZ(this.target);
				this.changeState(State.WATER2_REVERSE_PICK_CLICK);
				this.addSkipTicks(WATER_TICKS);
				break;
			case WATER2_REVERSE_PICK_CLICK:
				this.rightClick();
				if (!this.water_placed)
					this.changeState(State.WATER2_REVERSE_PLACE_LEFT);
				else
					this.changeState(State.WATER2_REVERSE_PLACE);
				break;
			case WATER2_REVERSE_PLACE :
			//	this.targetBlockPos = this.downRightBlockPos().offset(this.facing.rotateYClockwise(), 4);
				this.targetBlockPos = this.getRelativeBlockPos(this.traversing, 1, -1, 0);
				this.printPos("WATER2_REVERSE_PLACE", this.targetBlockPos);
				this.target = this.getBlockFaceRand(this.targetBlockPos, Direction.UP);
				this.lookAt(this.target);
				this.changeState(State.WATER2_REVERSE_PLACE_CLICK);
				this.skipTicks(WATER_TICKS);
				break;
			case WATER2_REVERSE_PLACE_CLICK :
				this.rightClick();
				this.changeState(State.WATER2_REVERSE_PICK);
				this.water_placed = false;
				break;
			case WATER2_REVERSE_PLACE_LEFT :
				//this.targetBlockPos = this.downRightBlockPos().offset(this.facing.rotateYClockwise(), 1);
				this.targetBlockPos = this.getRelativeBlockPos(this.traversing, 4, -1, 0);
				this.printPos("WATER2_REVERSE_PLACE_LEFT", this.targetBlockPos);
			//	this.target = Vec3d.ofBottomCenter(this.targetBlockPos);
				this.target = this.getBlockFaceRand(this.targetBlockPos, Direction.UP);
				this.lookAtRandXZ(this.target);
				this.changeState(State.WATER2_REVERSE_PLACE_LEFT_CLICK);
				this.skipTicks(WATER_TICKS);
				break;
			case WATER2_REVERSE_PLACE_LEFT_CLICK :
				this.rightClick();
				this.changeState(State.WATER2_REVERSE_PICK);
				this.water_placed = true;
				break;
			case WATER2_PICK :
				this.stop();
				this.straightFace(this.facing);
				this.curBlockPos = this.player.getBlockPos();
				if (this.isFrontBlockSolid()) {
					this.changeState(State.WATER2);
					this.water2_lane_end = true;
					break;
				}
				if (this.prevBlockPos == null)
					this.prevBlockPos = this.curBlockPos;
				if (this.curBlockPos == this.prevBlockPos && !this.water_placed) {
					this.moveForward();
					this.sameState();
					break;
				}
				this.prevBlockPos = this.curBlockPos;
				this.targetBlockPos = this.downRightBlockPos().offset(this.facing.rotateYClockwise(), 2);
				this.target = Vec3d.ofBottomCenter(this.targetBlockPos);
				this.printPos("WATER2_RIGHT target: ", this.target);
				this.lookAtRandXZ(this.target);
				this.changeState(State.WATER2_PICK_CLICK);
				this.skipTicks(WATER_TICKS);
				break;
			case WATER2_PICK_CLICK :
				this.rightClick();
				if (water_placed)
					this.changeState(State.WATER2_PLACE_LEFT);
				else
					this.changeState(State.WATER2_PLACE);
				break;
			case WATER2_PLACE :
				//this.targetBlockPos = this.downLeftBlockPos().offset(this.facing.rotateYCounterclockwise(), 4);
				this.targetBlockPos = this.getRelativeBlockPos(this.traversing, -4, -1, 0);
				this.printPos("WATER2_PLACE target: " , this.targetBlockPos);
				this.target = this.getBlockFaceRand(this.targetBlockPos, Direction.UP);
				this.lookAt(this.target);
				this.skipTicks(WATER_TICKS);
				this.changeState(State.WATER2_PLACE_CLICK);
				break;
			case WATER2_PLACE_CLICK :
				this.rightClick();
				this.changeState(State.WATER2_PICK);
				this.water_placed = true;
			//	this.addSkipTicks(25);
				break;
			case WATER2_PLACE_LEFT :
				this.targetBlockPos = this.downLeftBlockPos().offset(this.facing.rotateYCounterclockwise(), 1);
				this.target = Vec3d.ofBottomCenter(this.targetBlockPos);
				this.lookAtRandXZ(this.target);
				this.skipTicks(WATER_TICKS);
				this.changeState(State.WATER2_PLACE_LEFT_CLICK);
				break;
			case WATER2_PLACE_LEFT_CLICK :
				this.rightClick();
				this.changeState(State.WATER2_PICK);
				this.water_placed = false;
				break;
			case WATER :
				/*
				if (this.player.getInventory().getMainHandStack() == ItemStack.EMPTY ||  this.isFrontBlockSolid()) {
					LOGGER.info("stack count" + this.player.getInventory().getMainHandStack().getCount());
					LOGGER.info("front block solid " + this.isFrontBlockSolid());
					this.stop();
					this.state = State.SLEEP;
					break;
				} else {
					this.sprint();
					this.moveForward();
				}
				*/
				if (this.getPrevState() == State.WATER_LEFT) {
					this.retState();
					break;
				}
				this.facing = this.getFacing();
				this.traversing = this.facing.rotateYClockwise();
				this.straightFace(this.facing);
				this.callState(State.WATER_LEFT);
				break;
			case WATER_LEFT :
				this.stop();
				this.straightFace(this.facing);
				this.curBlockPos = this.player.getBlockPos();
				if (this.isDownFrontBlockSolid()) {
					this.retState();
					break;
				}
				if (this.prevBlockPos == null)
					this.prevBlockPos = this.curBlockPos;
				if (this.curBlockPos == this.prevBlockPos) {
					this.moveRight();
					this.sameState();
					break;
				}
				this.prevBlockPos = this.curBlockPos;
				this.targetBlockPos = this.downFrontLeftBlockPos();
				this.target = Vec3d.ofBottomCenter(this.targetBlockPos);
				this.printPos("WATER_LEFT target: ", this.target);
				this.lookAtRandXZ(this.target);
				this.rightClick();
				this.changeState(State.WATER_RIGHT);
				break;
			case WATER_RIGHT :
				this.straightFace(this.facing);
				this.target = Vec3d.ofBottomCenter(this.targetBlockPos.offset(this.traversing, 2));
				this.printPos("WATER_RIGHT target: ", this.target);
				this.lookAtRandXZ(target);
				this.changeState(State.WATER_RIGHT_CLICK);
				this.addSkipTicks(10);
				break;
			case WATER_RIGHT_CLICK :
				this.rightClick();
				this.changeState(State.WATER_LEFT);
				break;
			case PLANT :
				this.stop();
				this.curBlockPos = this.player.getBlockPos();
				if (this.prevBlockPos == null)
					this.prevBlockPos = this.curBlockPos;
				if (this.curBlockPos == this.prevBlockPos) {
					this.sprint();
					this.moveForward();
					break;
				}
				if (delayTick > 0) { //					this.leftClick();
					delayTick--;
					break;
				}
				this.delayTick = 1;
				this.prevBlockPos = this.curBlockPos;
				this.straightYaw();
				this.player.setPitch(80);
				LOGGER.info("rightclicking");
				this.rightClick();
				if (this.player.getInventory().getMainHandStack() == ItemStack.EMPTY ||  this.isFrontBlockSolid()) {
					LOGGER.info("stack count" + this.player.getInventory().getMainHandStack().getCount());
					LOGGER.info("front block solid " + this.isFrontBlockSolid());
					this.stop();
					this.state = State.SLEEP;
					break;
				} else {
					this.sprint();
					this.moveForward();
				}
				break;
			case PLANT2 :
				//LOGGER.info("prevstate" + this.getPrevState());
				if (this.getPrevState() == State.PLANT2_DOIT || this.out_of_stock) {
					LOGGER.info("out of stock");
					this.out_of_stock = false;
					this.retState();
					break;
				}
				this.facing = this.getFacing();
				this.traversing = this.facing.rotateYClockwise();
				this.straightFace(this.facing);
				this.changeState(State.PLANT2_DOIT);
				this.plant_place = 0;
				break;
			case PLANT2_NEXT :
				this.stop();
				this.straightFace(this.facing);
				this.curBlockPos = this.player.getBlockPos();
				if (this.prevBlockPos == null)
					this.prevBlockPos = this.curBlockPos;
				if (this.curBlockPos == this.prevBlockPos) {
					this.moveForward();
					this.sameState();
					break;
				}
				this.prevBlockPos = this.curBlockPos;
				if (this.plant2_walk-- <= 0) {
					this.traversing = this.traversing.getOpposite();
					this.changeState(State.PLANT2_DOIT);
					
				}
				break;
			case PLANT2_DOIT :
				this.stop();
				this.straightFace(this.facing);
				this.curBlockPos = this.player.getBlockPos();
				if ((this.traversing == this.facing.rotateYClockwise() && this.isRightBlockSolid())
					|| (this.traversing == this.facing.rotateYCounterclockwise() && this.isLeftBlockSolid())) {
					LOGGER.info("done with this lane, finding next");
					this.changeState(State.PLANT2_NEXT);
					this.plant2_walk = 6;
					break;
				}

				if (this.prevBlockPos == null)
					this.prevBlockPos = this.curBlockPos;
				if (this.curBlockPos == this.prevBlockPos) {
					if (this.traversing == this.facing.rotateYClockwise())
						this.moveRight();
					else
						this.moveLeft();
					this.sameState();
					break;
				}
				this.prevBlockPos = this.curBlockPos;
				this.callState(State.PLANT2_PLACE);
				this.plant_place = 0;
				break;
			case PLANT2_REPLENISH :
				LOGGER.info("opening inventory");
				this.stop();
			//	this.changeState(State.SLEEP);
				this.openInventory();
				this.changeState(State.PLANT2_REPLENISH_HOTBAR);
				this.skipTicks(5);
				this.replenished = true;
				break;
			case PLANT2_REPLENISH_HOTBAR :
				LOGGER.info("finding empty hotbar slot");
				int k = 36;
				while ( k <= 44) {
					if (this.isSlotEmpty(k)) {
						this.emptyHotbarSlot = k - 36;
						break;
					}
					k++;
				}
				// no more empty space
				if (k > 44) {
					LOGGER.info("no more empty slots in hotbar");
					this.closeInventory();
					this.changeState(State.PLANT2_PLACE);
				} else {
					this.changeState(State.PLANT2_REPLENISH_MAIN);
				}
				break;
			case PLANT2_REPLENISH_MAIN :
				LOGGER.info("finding sugar cane in main inventory");
				LOGGER.info("why this");
				int l = 9;
				while (l <= 35) {
				//LOGGER.info("slot num: " + l + " item: " + this.player.currentScreenHandler.getSlot(l).getStack().getItem());
					if (this.isSlotSugarCane(l)) {
						LOGGER.info("swapping slot " + l + " with hotbar " + this.emptyHotbarSlot);
						this.swapSlot(l,this.emptyHotbarSlot);
						this.skipTicks(5);	
						break;
					} else {
						LOGGER.info("slot " + l + " = " + this.player.currentScreenHandler.getSlot(l).getStack().getItem());
					}
					l++;
				}
				// no more sugar cane in inventory
				if (l > 35) {
					LOGGER.info("no more sugar cane in inventory");
					this.closeInventory();
					this.changeState(State.PLANT2_PLACE);

				} else {
					LOGGER.info("swap slot success, find next empty hotbar");
					this.changeState(State.PLANT2_REPLENISH_HOTBAR);
				}
				break;
			case PLANT2_PLACE :
				this.stop();
				this.sameState();
				if (this.player.getInventory().getMainHandStack() == ItemStack.EMPTY) {
					if (this.replenished) {
						LOGGER.info("completely out of stock");
						this.out_of_stock = true;
						//this.changeState(State.PLANT2);
						this.changeState(State.SLEEP);
						break;
					}
					LOGGER.info("stack empty");
					int found = -1;
					for (int i = 0; i < this.player.getInventory().getHotbarSize(); i++) {
	//							if (this.player.getInventory().getStack(i).getUseAction() == UseAction.BLOCK) {
						if (this.player.getInventory().getStack(i).getItem() == Items.SUGAR_CANE) {
							LOGGER.info("found sugar cane");
							found = i;
							break;
						}
					}
					if (found >= 0) {
						LOGGER.info("switching to different stack in hotbar");
						this.player.getInventory().selectedSlot = found; 
						//this.leftClick();
						//this.backState();
					} else {
						LOGGER.info("no more sugar_canes in hotbar");
						this.changeState(State.PLANT2_REPLENISH);
					}
					this.skipTicks(5);
					break;
				}
				this.replenished = false;
				switch (this.plant_place) {
				case 0 :
					this.targetBlockPos =  this.downBlockPos().offset(this.facing, 5);
					this.plant_place = 1;
					break;
				case 1 :
					this.targetBlockPos =  this.downBlockPos().offset(this.facing, 4);
					this.plant_place = 2;
					break;

				case 2 :
					this.targetBlockPos =  this.downBlockPos().offset(this.facing, 1);
					this.plant_place = 3;
					break;
				case 3 :
					this.targetBlockPos =  this.downBlockPos();
					this.retState();
					break;
				default :
					// never gets here
					break;
				}
				this.target = Vec3d.ofCenter(this.targetBlockPos, 0.5D);
				this.printPos("PLANT2_PLACE target: ", this.target);
				this.lookAtRandXZ(this.target);
				this.rightClick();
				break;
			case CLICK_CRAFTING_TABLE :
				String screenTitle = this.client.currentScreen.getTitle().getString();
				if (screenTitle.equals((String)"Craft Item")) {
					LOGGER.info("found crafting option");
					this.state = State.CRAFT_ENCHANTED_SUGAR;
					this.justSwitched = true;
					this.skipTicks(7);
					break;
				} else if (screenTitle.equals((String)"SkyBlock Menu")) {
					// click again
					this.skipTicks(20);
					this.client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, 31, 0, SlotActionType.PICKUP, client.player);
					break;
				}
				LOGGER.info("crafting failed, going to sleep");
				LOGGER.info("screen name: " + this.client.currentScreen.getTitle().getString());
				this.player.closeHandledScreen();
				this.state = State.SLEEP;
				break;

			case CRAFT_ENCHANTED_SUGAR :
				LOGGER.info("crafting enchanted sugar");
				//initialize variables
				if (this.justSwitched) {
					this.startSlot = 54;
					this.endSlot = 89;
					this.ingredients = 0;
					this.delayTick = 0;
					this.justSwitched = false;
					this.enchanted_sugar = 0;

				} else {
					if (this.ingredients == MAX_INGREDIENTS) {
						if (isSlotSugar(HYPIXEL_CRAFT_RESULT_SLOT)) {
							this.shiftClickSlot(HYPIXEL_CRAFT_RESULT_SLOT);
							this.enchanted_sugar++;
							this.skipTicks(10);
							break;
						}
						if (this.enchanted_sugar > 0 && !isSlotSugar(HYPIXEL_CRAFT_RESULT_SLOT)) {
							this.ingredients = this.enchanted_sugar = 0;
						}
					}
					if (startSlot > endSlot) {
						this.player.closeHandledScreen();
						this.player.getInventory().selectedSlot = 0; 
						this.state = State.FARM;
						this.client.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(0));
						break;
					}
					if (this.isSlotSugarCane(startSlot) && getSlotCount(startSlot) == 64) {
						this.shiftClickSlot(startSlot);
						this.ingredients++;
						this.skipTicks(10);
					}
					startSlot++;
				}

				break;
			case DUMP_TO_CHEST :
				LOGGER.info("dumping mode");
				this.stop();
				if (client.currentScreen != null) {
					LOGGER.info("currentScreen title" + client.currentScreen.getTitle().getString());
					if (client.currentScreen.getTitle().getString().equals((String)"Chest") || client.currentScreen.getTitle().getString().equals((String)"Large Chest")) {
						LOGGER.info("opened chest/large chest");
						if (!midDump) {
							LOGGER.info("starting to dump");
							if (client.player.currentScreenHandler.slots.size() == 63) { //  chest
								startSlot = 27;
								endSlot = 62;
							} else
							if (client.player.currentScreenHandler.slots.size() == 90) { // large chest
								startSlot = 54;
								endSlot = 89;
							}
							midDump = true;
						}
						if (startSlot <= endSlot) {
							if (delayTick == 0) {
								LOGGER.info("mid dump");
								if (client.player.currentScreenHandler.getSlot(startSlot).getStack().getItem() == Items.SUGAR_CANE) {
									client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, startSlot, 0, SlotActionType.QUICK_MOVE, client.player);
									delayTick = 7;
								}
								startSlot++;
							} else
								delayTick--;
						} else {
							LOGGER.info("startSlot: " + startSlot);
							LOGGER.info("endSlot: " + endSlot);
							LOGGER.info("done dumping");
							state = State.SLEEP;// go to sleep after dump
							this.stop();
							midDump = false;
							client.player.closeHandledScreen();
						}

					}

				}
				break;
			case REPLENISH :
				this.stop();
				LOGGER.info("opening inventory");
				if (this.return_state) {
					this.retState();
					this.return_state = false;
					break;
				}
			//	this.changeState(State.SLEEP);
				this.openInventory();
				this.changeState(State.REPLENISH_HOTBAR);
				this.skipTicks(5);
				this.replenished = true;
				break;
			case REPLENISH_HOTBAR :
				LOGGER.info("finding empty hotbar slot");
				int m = 36;
				while ( m <= 44) {
					if (this.isSlotEmpty(m)) {
						this.emptyHotbarSlot = m - 36;
						break;
					}
					m++;
				}
				// no more empty space
				if (m > 44) {
					LOGGER.info("no more empty slots in hotbar");
					this.closeInventory();
					this.return_state = true;
					this.changeState(State.REPLENISH);
					break;
				} else {
					this.changeState(State.REPLENISH_MAIN);
				}
				break;
			case REPLENISH_MAIN :
				LOGGER.info("finding " + this.findItem + " in main inventory");
				int n = 9;
				while (n <= 35) {
				//LOGGER.info("slot num: " + l + " item: " + this.player.currentScreenHandler.getSlot(l).getStack().getItem());
					if (this.isSlotItem(n, this.findItem)) {
						LOGGER.info("swapping slot " + n + " with hotbar " + this.emptyHotbarSlot);
						this.swapSlot(n,this.emptyHotbarSlot);
						this.skipTicks(5);	
						break;
					} else {
						LOGGER.info("slot " + n + " = " + this.player.currentScreenHandler.getSlot(n).getStack().getItem());
					}
					n++;
				}
				// no more sugar cane in inventory
				if (n > 35) {
					LOGGER.info("no more sugar cane in inventory");
					this.closeInventory();
					this.return_state = true;
					this.changeState(State.REPLENISH);

				} else {
					LOGGER.info("swap slot success, find next empty hotbar");
					this.changeState(State.REPLENISH_HOTBAR);
				}
				break;
			case SLEEP : // sleeping
				// do nothing at all
				break;
			default:
				break;
			}
			if (this.state != State.SLEEP && this.state != State.INITIALIZE) {
				if (this.justBorn) {
					this.prevX = this.client.player.getX();
					this.prevZ = this.client.player.getZ();
					this.prevY = this.client.player.getY();
					this.justBorn = false;
				}
				// stop if we're teleported to the lobby
				if (this.movedTooFast()) {
					this.stop();
					this.state = State.SLEEP;
					LOGGER.info("teleported");
				}
				this.prevX = this.client.player.getX();
				this.prevZ = this.client.player.getZ();
				this.prevY = this.client.player.getY();
			}

		}
		// goes to sleep if we were moved too fast, possibly by being teleported
		// to the lobby
		public boolean teleported()
		{
			if (this.justBorn) {
				this.prevX = this.client.player.getX();
				this.prevZ = this.client.player.getZ();
				this.prevY = this.client.player.getY();
				this.justBorn = false;
				return false;
			}
			if (movedTooFast()) {
				return true;
			}
			this.prevX = this.client.player.getX();
			this.prevZ = this.client.player.getZ();
			this.prevY = this.client.player.getY();
			return false;
		}
		public boolean movedTooFast()
		{
			if (Math.pow(this.client.player.getX() - this.prevX, 2)+ Math.pow(this.client.player.getY() - this.prevY, 2) + Math.pow(this.client.player.getZ() - this.prevZ, 2) > Math.pow(MAX_TICK_DISTANCE, 2)) {
				return true;
			}
			return false;
		}

		// true if delayTick <= 0
		public boolean doneDelay()
		{
			if (delayTick <= 0) {
				delayTick = 10;
				return true;
			}
			delayTick--;
			return false;
		}
		public void swapSlot(int slot, int hotbarSlot)
		{
			this.client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, slot, hotbarSlot, SlotActionType.SWAP, client.player);
			this.skipTicks(5);
		}
		public void shiftClickSlot(int slot) {
			this.client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, slot, 0, SlotActionType.QUICK_MOVE, client.player);
		}
		public int getSlotCount(int slot) {
			return this.player.currentScreenHandler.getSlot(slot).getStack().getCount();
		}
		public boolean isSlotEmpty(int slot)
		{
			return this.player.currentScreenHandler != null ?  this.player.currentScreenHandler.getSlot(slot).getStack() == ItemStack.EMPTY : false;
		}
		public boolean isSlotSugarCane(int slot) {
			return this.player.currentScreenHandler != null ?  this.player.currentScreenHandler.getSlot(slot).getStack().getItem() == Items.SUGAR_CANE : false;
		}
		public boolean isSlotItem(int slot, Item item) {
			return this.player.currentScreenHandler != null ?  this.player.currentScreenHandler.getSlot(slot).getStack().getItem() == item : false;
		}
		public boolean isSlotSugar(int slot) {
			return this.player.currentScreenHandler != null ?  this.player.currentScreenHandler.getSlot(slot).getStack().getItem() == Items.SUGAR : false;
		}
		public void doNothing() {
			this.state = State.SLEEP;
		}
		public boolean isOneZSolid(boolean north)
		{
			int stepZ = north? -1 : 1;
			return this.client.world.getBlockState(new BlockPos(this.player.getX(), this.player.getY(), this.player.getZ() + stepZ)).getMaterial().isSolid();
		}
		public boolean isBlockPosSolid(BlockPos blockPos)
		{
			return this.client.world.getBlockState(blockPos).getMaterial().isSolid();
		}

		public boolean isDownBlockSolid()
		{
			return this.client.world.getBlockState(downBlockPos()).getMaterial().isSolid();
		}
		public boolean isRightBlockSolid()
		{
			return this.client.world.getBlockState(rightBlockPos()).getMaterial().isSolid();
		}
		public boolean isFrontLeftBlockSolid()
		{
			return this.client.world.getBlockState(frontLeftBlockPos()).getMaterial().isSolid();
		}
		public boolean isFrontRightBlockSolid()
		{
			return this.client.world.getBlockState(frontRightBlockPos()).getMaterial().isSolid();
		}
		public boolean isLeftBlockSolid()
		{
			return this.client.world.getBlockState(leftBlockPos()).getMaterial().isSolid();
		}
		public boolean isFrontBlockSolid()
		{
			return this.client.world.getBlockState(frontBlockPos()).getMaterial().isSolid();
		}
		public boolean isDownFrontBlockSolid()
		{
			return this.client.world.getBlockState(downFrontBlockPos()).getMaterial().isSolid();
		}
		public boolean isBackBlockSolid()
		{
			return this.client.world.getBlockState(backBlockPos()).getMaterial().isSolid();
		}
		public boolean isFacingEast() {
			return (this.player.getYaw() < -45 && this.player.getYaw() > -135) || (this.player.getYaw() < 315 && this.player.getYaw() > 225);
		}
		public boolean isFacingWest() {
			return this.player.getYaw() > 45 && this.player.getYaw() < 135;
		}
		public boolean isFacingSouth() {
			return (Math.abs(this.player.getYaw()) <= 45) || (this.player.getYaw() > 315);
		}
		public boolean isFacingNorth() {
			return Math.abs(this.player.getYaw()) >= 135 && this.player.getYaw() <= 225;
		}
		public BlockPos downBlockPos()
		{
			return new BlockPos(this.player.getX(), this.player.getY()- 1, this.player.getZ());
		}
		public BlockPos downRightBlockPos()
		{
			Direction facing = this.player.getHorizontalFacing();
			BlockPos target = this.player.getBlockPos().offset(Direction.DOWN).offset(facing.rotateYClockwise());
			return target;
		}
		public BlockPos getRelativeBlockPos(Direction facing, int x, int y, int z)
		{
			BlockPos retPos = this.player.getBlockPos();
			switch (facing) {
			case NORTH :
				retPos = this.player.getBlockPos().offset(Direction.EAST, x).offset(Direction.UP, y).offset(Direction.SOUTH, z);
				break;
			case SOUTH :
				retPos = this.player.getBlockPos().offset(Direction.WEST, x).offset(Direction.UP, y).offset(Direction.NORTH, z);
				break;
			case EAST :
				retPos = this.player.getBlockPos().offset(Direction.SOUTH, x).offset(Direction.UP, y).offset(Direction.WEST, z);
				break;
			case WEST :
				retPos = this.player.getBlockPos().offset(Direction.NORTH, x).offset(Direction.UP, y).offset(Direction.EAST, z);
				break;
			default :
				//never gets here
				break;
			}
			return retPos;
		}
		public BlockPos downLeftBlockPos()
		{
			Direction facing = this.player.getHorizontalFacing();
			BlockPos target = this.player.getBlockPos().offset(Direction.DOWN).offset(facing.rotateYCounterclockwise());
			return target;
		}

		public BlockPos downFrontLeftBlockPos()
		{
			Direction facing = this.player.getHorizontalFacing();
			BlockPos target = this.downLeftBlockPos().offset(facing);
			return target;
		}
		public BlockPos downFrontRightBlockPos()
		{
			Direction facing = this.player.getHorizontalFacing();
			BlockPos target = this.downRightBlockPos().offset(facing);
			return target;
		}
		public BlockPos rightBlockPos()
		{
			int x, y, z;
			x = y = z = 0;
			if (this.isFacingEast())
				z = 1;
			else if (this.isFacingSouth())
				x = -1;
			else if (this.isFacingNorth())
				x = 1;
			else
				z = -1;
			return new BlockPos(this.player.getX() + x, this.player.getY() + y, this.player.getZ() + z);
		}
		public BlockPos leftBlockPos()
		{
			int x, y, z;
			x = y = z = 0;
			if (this.isFacingEast())
				z = -1;
			else if (this.isFacingSouth())
				x = 1;
			else if (this.isFacingNorth())
				x = -1;
			else
				z = 1;
			return new BlockPos(this.player.getX() + x, this.player.getY() + y, this.player.getZ() + z);
		}
		public BlockPos downBackBlockPos()
		{
			Direction facing = this.player.getHorizontalFacing();
			BlockPos target = this.player.getBlockPos().offset(facing.getOpposite()).offset(Direction.DOWN);
			return target;
		}
		public BlockPos backLeftBlockPos()
		{
			Direction facing = this.player.getHorizontalFacing();
			BlockPos target = this.player.getBlockPos().offset(facing.getOpposite()).offset(facing.rotateYCounterclockwise());
			return target;
		}
		public BlockPos backRightBlockPos()
		{
			Direction facing = this.player.getHorizontalFacing();
			BlockPos target = this.player.getBlockPos().offset(facing.getOpposite()).offset(facing.rotateYClockwise());
			return target;
		}
		public BlockPos downBackLeftBlockPos()
		{
			Direction facing = this.player.getHorizontalFacing();
			BlockPos target = this.player.getBlockPos().offset(facing.getOpposite()).offset(Direction.DOWN).offset(facing.rotateYCounterclockwise());
			return target;
		}
		public BlockPos downBackRightBlockPos()
		{
			Direction facing = this.player.getHorizontalFacing();
			BlockPos target = this.player.getBlockPos().offset(facing.getOpposite()).offset(Direction.DOWN).offset(facing.rotateYClockwise());
			return target;
		}
		public BlockPos downFrontBlockPos()
		{
			int x, y, z;
			x = y = z = 0;
			y = -1;
			if (this.isFacingEast())
				x = 1;
			else if (this.isFacingSouth())
				z = 1;
			else if (this.isFacingNorth())
				z = -1;
			else
				x = -1;
			return new BlockPos(this.player.getX() + x, this.player.getY() + y, this.player.getZ() + z);
		}
		public BlockPos frontLeftBlockPos()
		{
			int x, y, z;
			x = y = z = 0;
			if (this.isFacingEast()) {
				x = 1;
				z = -1;
			} else if (this.isFacingSouth()) {
				z = 1;
				x = 1;
			} else if (this.isFacingNorth()) {
				z = -1;
				x = -1;
			} else if (this.isFacingWest()) {
				x = -1;
				z = 1;
			}
			return new BlockPos(this.player.getX() + x, this.player.getY() + y, this.player.getZ() + z);
		}
		public BlockPos frontRightBlockPos()
		{
			int x, y, z;
			x = y = z = 0;
			if (this.isFacingEast()) {
				x = 1;
				z = 1;
			} else if (this.isFacingSouth()) {
				z = 1;
				x = -1;
			} else if (this.isFacingNorth()) {
				z = -1;
				x = 1;
			} else if (this.isFacingWest()) {
				x = -1;
				z = -1;
			}
			return new BlockPos(this.player.getX() + x, this.player.getY() + y, this.player.getZ() + z);
		}
		public BlockPos frontBlockPos()
		{
			int x, y, z;
			x = y = z = 0;
			if (this.isFacingEast())
				x = 1;
			else if (this.isFacingSouth())
				z = 1;
			else if (this.isFacingNorth())
				z = -1;
			else
				x = -1;
			return new BlockPos(this.player.getX() + x, this.player.getY() + y, this.player.getZ() + z);
		}
		public BlockPos backBlockPos()
		{
			int x, y, z;
			x = y = z = 0;
			if (this.isFacingEast())
				x = -1;
			else if (this.isFacingSouth())
				z = -1;
			else if (this.isFacingNorth())
				z = 1;
			else // west
				x = 1;
			return new BlockPos(this.player.getX() + x, this.player.getY() + y, this.player.getZ() + z);
		}
		public boolean isSugarFace() {
			/*
			if (isfaceingeast()) { 				// sugar cane in front block 
				return  registry.block.getid(this.client.world.getblockstate(new blockpos(this.player.getX() + 1, this.player.getY(), this.player.getZ())).getblock()).getpath().equals("sugar_cane");
			} else {
				return registry.block.getid(this.client.world.getblockstate(new blockpos(this.player.getX() - 1, this.player.getY(), this.player.getZ())).getblock()).getpath().equals("sugar_cane");
			} 
			*/
			int step = isFacingEast() ? 1 : -1;
			return !this.client.world.getBlockState(new BlockPos(this.player.getX() + step, this.player.getY(), this.player.getZ())).getMaterial().isSolid();
		}
		public boolean isSugarBack() {
				// sugar cane in front block and standing 
			if (this.player.getYaw() < -45 && this.player.getYaw() > -135) { // facing east
				return  Registry.BLOCK.getId(this.client.world.getBlockState(new BlockPos(this.player.getX() - 1, this.player.getY(), this.player.getZ())).getBlock()).getPath().equals("sugar_cane");
			} else {
				return Registry.BLOCK.getId(this.client.world.getBlockState(new BlockPos(this.player.getX() + 1, this.player.getY(), this.player.getZ())).getBlock()).getPath().equals("sugar_cane");
			} 
		}

		public void defaultFace() {
			player.setYaw(defYaw);
			player.setPitch(defPitch);
		}

		// constructor
	

		public void sleep() {
			if (state == State.INITIALIZE) //not born yet
				return;
			state = State.SLEEP;
			stop();
//			this.player.getInventory().dropAll();
//			if (this.player.world.isClient)
//				this.player.getPitch() += 10;
//			this.player.swingHand(Hand.MAIN_HAND);
		}
		public void fly() {
				client.player.move(MovementType.PLAYER, new Vec3d(20D, 20, 0));
		}
		public void wake() {
			if (state == State.INITIALIZE) //not born yet
				return;
			state = State.FARM;
			this.dYaw = 13.0F;
			this.defaultFace();
			//this.player.getYaw() += -30; // start facing X axis
			this.sprint();
			//this.moveRight();
			this.moveForward();
			this.attack();

		}
		public void selectHotbar(int i)
		{
			this.player.getInventory().selectedSlot = i; 
		}
		public void swapHotbar(int i)
		{
			this.client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, i, 0, SlotActionType.PICKUP, client.player);
		}
		public void swapHotbar(int hotbar, int main)
		{
			this.selectHotbar(hotbar);
			this.swapHotbar(main);
		}
		public void clickHotbar(int i) {
			client.options.keysHotbar[i].setPressed(true);
			this.skipTicks(MAX_TICK_DELAY);
		}
		public void slotClick(int slot)
		{
			this.client.interactionManager.clickSlot(client.player.currentScreenHandler.syncId, slot, 0, SlotActionType.PICKUP, client.player);
		}
		public void rightClickHotbar(int i) {
			if (0 <= i && i < 9) {
			//	this.client.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(8));
				this.player.getInventory().selectedSlot = i; 
				this.rightClick();
			}
		}

		public void rightClick() {
			this.client.options.keyUse.setPressed(true);
			this.skipTicks(MAX_TICK_DELAY);
		}
		public void rightClickBlock() {
			this.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, (BlockHitResult)this.client.crosshairTarget));
			this.skipTicks(MAX_TICK_DELAY);
		}
		public void breakBlock(BlockPos pos, Direction direction) {
			this.client.interactionManager.attackBlock(pos, direction);
		}
		public void leftClick() {
			client.options.keyAttack.setPressed(true);
			//this.skipTicks(MAX_TICK_DELAY);
		}
		public void sprint() {
			client.options.keySprint.setPressed(true);
		}

		public void moveRight() {
			client.options.keyRight.setPressed(true);

		}
		public void attack() {
			client.options.keyAttack.setPressed(true);

		}
		public void moveForward() {
			client.options.keyForward.setPressed(true);

		}
		public void moveLeft() {
			client.options.keyLeft.setPressed(true);

		}
		public void moveBack() {
			client.options.keyBack.setPressed(true);

		}
		public void sneak()
		{
			client.options.keySneak.setPressed(true);
		}
		public void openInventory()
		{
			//client.options.keyInventory.setPressed(true);
			 this.client.openScreen(new InventoryScreen(this.player));
		}
		public void closeInventory()
		{
			this.player.closeHandledScreen();
		}
		public void stop() {
				client.options.keyRight.setPressed(false);
				client.options.keyLeft.setPressed(false);
				client.options.keyAttack.setPressed(false);
				client.options.keyForward.setPressed(false);
				client.options.keyInventory.setPressed(false);
				client.options.keySprint.setPressed(false);
				client.options.keyBack.setPressed(false);
				client.options.keySneak.setPressed(false);
				for (int i = 0; i < 9; i++)
					client.options.keysHotbar[i].setPressed(false);
				client.options.keyUse.setPressed(false);

		}
		public void born(MinecraftClient cli) {
			this.callerStates = new ArrayDeque<State>();
			born = true;
			client = cli;
			state = State.SLEEP; // asleep after born
			player = cli.player;
			defYaw = -90;
			defPitch = 0;
			//defaultFace();
			midDump = false;
			startSlot = 0;
			endSlot = 0;
			delayTick = 0;
			rightLaneTraversal = true;
			ingredients = 0;
			enchanted_sugar = 0;
			//curBlockPos = prevBlockPos = this.player.getBlockPos();
		}
		public void chestDump() {
			state = state.DUMP_TO_CHEST;
		}
		public void printPos(String name, Vec3d pos)
		{
			LOGGER.info(name + ": (" +  pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")");
		}
		public void printPos(String name, Vec3i pos)
		{
			LOGGER.info(name + ": (" +  pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")");
		}
	}
	private static final Logger LOGGER = LogManager.getLogger();
	@Override
	public void onInitializeClient() {
		LOGGER.info("LOGGER INFO TEST");

		KeyBinding f12 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F12, "key.category.first.test"));
		KeyBinding f9 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F9, "key.category.first.test"));

		KeyBinding O = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_3", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "key.category.first.test"));
		KeyBinding i = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_4", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "key.category.first.test"));
		KeyBinding y = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_6", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Y, "key.category.first.test"));
		KeyBinding u = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_5", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_U, "key.category.first.test"));
		KeyBinding r = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_8", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.category.first.test"));
		KeyBinding N = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_9", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "key.category.first.test"));
		KeyBinding M = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_10", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.category.first.test"));
		KeyBinding semicolon = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_7", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_SEMICOLON, "key.category.first.test"));
		MyBot tachikoma = new MyBot();
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
//			LOGGER.info("END OF CLIENT TICK");
			if  (tachikoma.client == null || tachikoma.player == null)
				tachikoma.born(client);
			if (O.wasPressed())
				tachikoma.born(client);
			if (tachikoma.born) {
				if (f12.wasPressed()) {
					tachikoma.wake();
					// resume bot action
				} else if (f9.wasPressed()) {
					// pause bot
					tachikoma.sleep();
				} else if (i.wasPressed()) {
					//tachikoma.fly();
					tachikoma.state = State.SLEEP;
					tachikoma.bridgeM = 160;
					tachikoma.callState(State.BRIDGE_M);
				} else if (r.wasPressed()) {
					tachikoma.state = State.SLEEP;
				//	tachikoma.callState(State.WATER);
					//tachikoma.callState(State.WATER2);
					tachikoma.callState(State.WATER2_START);
				} else if (y.wasPressed()) {
				//	tachikoma.state = State.PLANT;
					tachikoma.state = State.SLEEP;
					tachikoma.callState(State.PLANT2);
					//client.player.getInventory().swapSlotWithHotbar(10);
				} else if (M.wasPressed()) {
					//tachikoma.state = State.BRIDGE_V;
					tachikoma.state = State.SLEEP;
					tachikoma.callState(State.WATER);
				} else if (N.wasPressed()) {
					tachikoma.state = State.BRIDGE_V;
				//	tachikoma.openInventory();

				}
			}
			tachikoma.tick();
			
		});
	}
}
