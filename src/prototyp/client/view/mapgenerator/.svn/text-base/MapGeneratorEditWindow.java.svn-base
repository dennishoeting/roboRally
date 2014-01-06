package prototyp.client.view.mapgenerator;

import java.util.LinkedHashMap;

import prototyp.client.util.Button;
import prototyp.client.util.Form;
import prototyp.client.util.HorizontalStack;
import prototyp.client.util.Minimap;
import prototyp.client.util.RadioToggleButton;
import prototyp.client.util.StandardLabel;
import prototyp.client.util.ToggleButton;
import prototyp.client.util.VerticalStack;
import prototyp.client.view.Page;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * MapGeneratorEditingWindow. Hier befindet sich alles, was für die visuelle
 * Darstellung wichtig ist.
 * 
 * @author Dennis (Verantwortlicher), Marcus
 * @version 1.0
 */
public class MapGeneratorEditWindow extends Window {
	/**
	 * BasicFieldStack
	 * 
	 * @author Dennis
	 */
	private class BasicFieldStack extends WindowContentStack {
		private BasicFieldStack() {
			super();

			buildWallArea(true);
			buildLaserArea(true);
		}
	}

	/**
	 * CheckpointFieldStack
	 * 
	 * @author Dennis
	 */
	private class CheckpointFieldStack extends WindowContentStack {
		private CheckpointFieldStack() {
			super();

			buildWallArea(false);
			buildCheckpointNumberArea(true);
		}
	}

	/**
	 * CompactorFieldStack
	 * 
	 * @author Dennis
	 */
	private class CompactorFieldStack extends WindowContentStack {
		private CompactorFieldStack() {
			super();

			buildWallArea(false);
			buildLaserArea(false);
			buildCompactorDirectionArea(true);
		}
	}

	/**
	 * RotateConveyorBeltFieldStack
	 * 
	 * @author Dennis
	 */
	private class ConveyorBeltFieldStack extends WindowContentStack {
		private ConveyorBeltFieldStack() {
			super();

			buildWallArea(false);
			buildLaserArea(false);
			buildConveyorBeltArea(true);
		}
	}

	/**
	 * GearFieldStack
	 * 
	 * @author Dennis
	 */
	private class GearFieldStack extends WindowContentStack {
		private GearFieldStack() {
			super();

			buildWallArea(false);
			buildLaserArea(false);
			buildGearDirectionArea(true);
		}
	}

	/**
	 * HoleFieldStack
	 * 
	 * @author Dennis
	 */
	private class HoleFieldStack extends WindowContentStack {
		private HoleFieldStack() {
			super();
		}
	}

	/**
	 * PusherFieldStack
	 * 
	 * @author Dennis
	 */
	private class PusherFieldStack extends WindowContentStack {
		private PusherFieldStack() {
			super();

			buildWallArea(false);
			buildLaserArea(false);
			buildPusherDirectionArea(true);
		}
	}

	/**
	 * RepairFieldStack
	 * 
	 * @author Dennis
	 */
	private class RepairFieldStack extends WindowContentStack {
		private RepairFieldStack() {
			super();

			buildWallArea(false);
			buildRepairValueArea(true);
		}
	}

	/**
	 * StartFieldStack
	 * 
	 * @author Dennis
	 */
	private class StartFieldStack extends WindowContentStack {
		private StartFieldStack() {
			super();

			buildWallArea(false);
			buildStartNumberArea(true);
		}
	}

	/**
	 * Stack für die verschiedenen mainArea-Inhalte
	 * 
	 * @author Dennis
	 * @version 1.0
	 */
	public class WindowContentStack extends VerticalStack {
		/**
		 * ButtonStack
		 * 
		 * @author Dennis
		 */
		private class ButtonStack extends HorizontalStack {
			private ButtonStack() {
				super();
			}

			/**
			 * Konstruktor
			 * 
			 * @param margin
			 * @param padding
			 */
			private ButtonStack(int margin, int padding) {
				super(margin, padding);
			}

			/**
			 * Konstruktor
			 * 
			 * @param title
			 */
			private ButtonStack(String title) {
				super(title);
			}

			/**
			 * Fügt die Buttons hinzu
			 * 
			 * @param buttons
			 */
			private void addButtons(final ImgButton... buttons) {
				addMembers(buttons);
			}
		}

		protected RadioToggleButton check1, check2, check3, check4, check5,
				check6, check7, check8;
		protected RadioToggleButton compactorAlways, compactorFirstLast;
		protected RadioToggleButton compactorNorth, compactorEast,
				compactorSouth, compactorWest;
		protected RadioToggleButton gearLeft, gearRight;

		protected RadioToggleButton laserEast0, laserEast1, laserEast2,
				laserEast3;
		protected RadioToggleButton laserNorth0, laserNorth1, laserNorth2,
				laserNorth3;
		protected RadioToggleButton laserSouth0, laserSouth1, laserSouth2,
				laserSouth3;
		protected RadioToggleButton laserWest0, laserWest1, laserWest2,
				laserWest3;
		protected RadioToggleButton pusherAlways, pusherUneven;
		protected RadioToggleButton pusherNorth, pusherEast, pusherSouth,
				pusherWest;
		protected RadioToggleButton repairValue1, repairValue2;
		protected RadioToggleButton conveyorBeltNorth, conveyorBeltWest,
				conveyorBeltEast, conveyorBeltSouth;

		protected ToggleButton conveyorBeltInNorth, conveyorBeltInWest,
				conveyorBeltInEast, conveyorBeltInSouth;

		protected RadioToggleButton start1, start2, start3, start4, start5,
				start6, start7, start8, start9, start10, start11, start12;
		protected RadioToggleButton conveyorBeltRange1, conveyorBeltRange2;
		protected ToggleButton wallNorth, wallEast, wallSouth, wallWest;
		protected RadioToggleButton restartNorth, restartEast, restartSouth,
				restartWest;

		private SectionStack menueStack;

		private SectionStackSection wallSection, laserSection, startSection,
				checkpointSection, gearSection, pusherSection,
				compactorSection, repairFieldSection, conveyorBeltSection;

		/**
		 * Konstruktor
		 */
		protected WindowContentStack() {
			super(0, 0);
			setOverflow(Overflow.VISIBLE);

			this.menueStack = new SectionStack();
			this.menueStack.setVisibilityMode(VisibilityMode.MULTIPLE);
			this.menueStack.setWidth100();
			this.menueStack.setHeight100();
			this.menueStack.setOverflow(Overflow.AUTO);

			this.setHeight(500);

			this.addMember(this.menueStack);
		}

		/**
		 * Stellt die buildCheckpointNumberArea dar
		 * 
		 * @param expanded
		 */
		protected void buildCheckpointNumberArea(boolean expanded) {
			this.checkpointSection = new SectionStackSection(
					Page.props.mapGeneratorEditingWindow_Feldspezifiktion());
			this.checkpointSection.setExpanded(expanded);

			VerticalStack tempStack = new VerticalStack(0, 0);
			VerticalStack stack = new VerticalStack(
					Page.props.mapGeneratorEditingWindow_CheckpointNumber());
			ButtonStack stackbtn1 = new ButtonStack(Integer.valueOf(Page.props
					.global_marginInStackAreas()), 0);
			ButtonStack stackbtn2 = new ButtonStack(Integer.valueOf(Page.props
					.global_marginInStackAreas()), 0);

			this.check1 = new RadioToggleButton(
					"mapgenerator/checkpoint_01.png", "1");
			this.check2 = new RadioToggleButton(
					"mapgenerator/checkpoint_02.png", "2");
			this.check3 = new RadioToggleButton(
					"mapgenerator/checkpoint_03.png", "3");
			this.check4 = new RadioToggleButton(
					"mapgenerator/checkpoint_04.png", "4");
			this.check5 = new RadioToggleButton(
					"mapgenerator/checkpoint_05.png", "5");
			this.check6 = new RadioToggleButton(
					"mapgenerator/checkpoint_06.png", "6");
			this.check7 = new RadioToggleButton(
					"mapgenerator/checkpoint_07.png", "7");
			this.check8 = new RadioToggleButton(
					"mapgenerator/checkpoint_08.png", "8");

			RadioToggleButton.addButtonsToRadioGroup("CheckNumbers",
					this.check1, this.check2, this.check3, this.check4,
					this.check5, this.check6, this.check7, this.check8);

			stackbtn1.addButtons(this.check1, this.check2, this.check3,
					this.check4);
			stackbtn1.setAutoHeight();
			stackbtn1.setWidth100();

			stackbtn2.addButtons(this.check5, this.check6, this.check7,
					this.check8);
			stackbtn2.setAutoHeight();
			stackbtn2.setWidth100();

			stack.addMembers(stackbtn1, stackbtn2);

			tempStack.addMember(stack);
			tempStack.setAutoHeight();
			this.checkpointSection.addItem(tempStack);
			this.menueStack.addSection(this.checkpointSection);
		}

		/**
		 * Stellt die buildCompactorDirectionArea dar
		 * 
		 * @param expanded
		 */
		protected void buildCompactorDirectionArea(boolean expanded) {
			this.compactorSection = new SectionStackSection(
					Page.props.mapGeneratorEditingWindow_Feldspezifiktion());
			this.compactorSection.setExpanded(expanded);

			VerticalStack tempStack = new VerticalStack(0, 0);

			ButtonStack stack1 = new ButtonStack(
					Page.props
							.mapGeneratorEditingWindow_compactorSection_ButtonStack());

			this.compactorNorth = new RadioToggleButton(
					"mapgenerator/compactor_north.png", "N");
			this.compactorEast = new RadioToggleButton(
					"mapgenerator/compactor_east.png", "O");
			this.compactorSouth = new RadioToggleButton(
					"mapgenerator/compactor_south.png", "S");
			this.compactorWest = new RadioToggleButton(
					"mapgenerator/compactor_west.png", "W");

			RadioToggleButton.addButtonsToRadioGroup("CompactorDirection",
					this.compactorNorth, this.compactorEast,
					this.compactorSouth, this.compactorWest);

			stack1.addButtons(this.compactorNorth, this.compactorEast,
					this.compactorSouth, this.compactorWest);
			stack1.setAutoHeight();
			stack1.setWidth100();

			ButtonStack stack2 = new ButtonStack(
					Page.props
							.mapGeneratorEditingWindow_compactorSection_active());

			this.compactorFirstLast = new RadioToggleButton(
					"mapgenerator/marker_enabled.png", "J");
			this.compactorAlways = new RadioToggleButton(
					"mapgenerator/marker_disabled.png", "N");

			RadioToggleButton.addButtonsToRadioGroup("compactorAlwaysOn",
					this.compactorAlways, this.compactorFirstLast);

			stack2.addButtons(this.compactorFirstLast, this.compactorAlways);
			stack2.setAutoHeight();
			stack2.setWidth100();

			tempStack.addMembers(stack1, stack2);
			tempStack.setAutoHeight();
			this.compactorSection.addItem(tempStack);
			this.menueStack.addSection(this.compactorSection);
		}

		/**
		 * Stellt die RotateConveyorBeltArea dar
		 * 
		 * @param expanded
		 */
		protected void buildConveyorBeltArea(boolean expanded) {
			this.conveyorBeltSection = new SectionStackSection(
					Page.props.mapGeneratorEditingWindow_Feldspezifiktion());
			this.conveyorBeltSection.setExpanded(expanded);

			VerticalStack tempStack = new VerticalStack(0, 0);

			VerticalStack stack1 = new VerticalStack(
					Page.props.mapGeneratorEditingWindow_OutDirection());
			VerticalStack stack2 = new VerticalStack(
					Page.props.mapGeneratorEditingWindow_InDirection());

			ButtonStack s1 = new ButtonStack(Integer.valueOf(Page.props
					.global_marginInStackAreas()), 0);
			ButtonStack s2 = new ButtonStack(Integer.valueOf(Page.props
					.global_marginInStackAreas()), 0);

			this.conveyorBeltNorth = new RadioToggleButton(
					"mapgenerator/arrow_north.png", "outN");
			this.conveyorBeltEast = new RadioToggleButton(
					"mapgenerator/arrow_east.png", "outE");
			this.conveyorBeltSouth = new RadioToggleButton(
					"mapgenerator/arrow_south.png", "outS");
			this.conveyorBeltWest = new RadioToggleButton(
					"mapgenerator/arrow_west.png", "outW");

			RadioToggleButton.addButtonsToRadioGroup("",
					this.conveyorBeltNorth, this.conveyorBeltEast,
					this.conveyorBeltSouth, this.conveyorBeltWest);

			this.conveyorBeltInNorth = new ToggleButton(
					"mapgenerator/arrow_north.png", "inN");
			this.conveyorBeltInEast = new ToggleButton(
					"mapgenerator/arrow_east.png", "inE");
			this.conveyorBeltInSouth = new ToggleButton(
					"mapgenerator/arrow_south.png", "inS");
			this.conveyorBeltInWest = new ToggleButton(
					"mapgenerator/arrow_west.png", "inW");

			s1.addButtons(this.conveyorBeltNorth, this.conveyorBeltEast,
					this.conveyorBeltSouth, this.conveyorBeltWest);

			s2.addButtons(this.conveyorBeltInNorth, this.conveyorBeltInEast,
					this.conveyorBeltInSouth, this.conveyorBeltInWest);

			s1.setAutoHeight();
			s1.setAutoWidth();

			s2.setAutoHeight();
			s2.setAutoWidth();

			stack1.addMembers(s1);
			stack1.setAutoHeight();
			stack1.setWidth100();

			stack2.addMembers(s2);
			stack2.setAutoHeight();
			stack2.setWidth100();

			ButtonStack stack3 = new ButtonStack(
					Page.props
							.mapGeneratorEditingWindow_ConveyorBeltFieldRange());

			this.conveyorBeltRange1 = new RadioToggleButton(
					"mapgenerator/move_value_01.png", "1");
			this.conveyorBeltRange2 = new RadioToggleButton(
					"mapgenerator/move_value_02.png", "2");

			RadioToggleButton.addButtonsToRadioGroup(
					"StraightConveyorBeltRange", this.conveyorBeltRange1,
					this.conveyorBeltRange2);

			stack3.addButtons(this.conveyorBeltRange1, this.conveyorBeltRange2);
			stack3.setAutoHeight();
			stack3.setWidth100();

			tempStack.addMembers(stack1, stack2, stack3);
			tempStack.setAutoHeight();
			this.conveyorBeltSection.addItem(tempStack);
			this.menueStack.addSection(this.conveyorBeltSection);

		}

		/**
		 * Stellt die buildGearDirectionArea dar
		 * 
		 * @param expanded
		 */
		protected void buildGearDirectionArea(boolean expanded) {
			this.gearSection = new SectionStackSection(
					Page.props.mapGeneratorEditingWindow_Feldspezifiktion());
			this.gearSection.setExpanded(expanded);

			VerticalStack tempStack = new VerticalStack(0, 0);
			ButtonStack stack = new ButtonStack(
					Page.props.mapGeneratorEditingWindow_direction());
			this.gearLeft = new RadioToggleButton("mapgenerator/gear_left.png",
					"L");
			this.gearRight = new RadioToggleButton(
					"mapgenerator/gear_right.png", "R");

			RadioToggleButton.addButtonsToRadioGroup("GearDirection",
					this.gearLeft, this.gearRight);

			stack.addButtons(this.gearRight, this.gearLeft);
			stack.setAutoHeight();
			stack.setWidth100();
			tempStack.addMember(stack);
			tempStack.setAutoHeight();
			this.gearSection.addItem(tempStack);
			this.menueStack.addSection(this.gearSection);
		}

		/**
		 * Stellt die buildLaserArea dar
		 * 
		 * @param expanded
		 */
		protected void buildLaserArea(boolean expanded) {
			this.laserSection = new SectionStackSection(
					Page.props.mapGeneratorEditingWindow_laserSection());
			this.laserSection.setExpanded(expanded);

			VerticalStack tempStack = new VerticalStack(0, 0);
			ButtonStack stack1 = new ButtonStack(
					Page.props.mapGeneratorEditingWindow_north());
			this.laserNorth0 = new RadioToggleButton(
					"mapgenerator/laser_north_00.png", "NORTH0");
			this.laserNorth1 = new RadioToggleButton(
					"mapgenerator/laser_north_01.png", "NORTH1");
			this.laserNorth2 = new RadioToggleButton(
					"mapgenerator/laser_north_02.png", "NORTH2");
			this.laserNorth3 = new RadioToggleButton(
					"mapgenerator/laser_north_03.png", "NORTH3");

			RadioToggleButton.addButtonsToRadioGroup("LaserNorth",
					this.laserNorth0, this.laserNorth1, this.laserNorth2,
					this.laserNorth3);

			stack1.addButtons(this.laserNorth0, this.laserNorth1,
					this.laserNorth2, this.laserNorth3);
			stack1.setAutoHeight();
			stack1.setWidth100();

			ButtonStack stack2 = new ButtonStack(
					Page.props.mapGeneratorEditingWindow_east());
			this.laserEast0 = new RadioToggleButton(
					"mapgenerator/laser_east_00.png", "EAST0");
			this.laserEast1 = new RadioToggleButton(
					"mapgenerator/laser_east_01.png", "EAST1");
			this.laserEast2 = new RadioToggleButton(
					"mapgenerator/laser_east_02.png", "EAST2");
			this.laserEast3 = new RadioToggleButton(
					"mapgenerator/laser_east_03.png", "EAST3");

			RadioToggleButton.addButtonsToRadioGroup("LaserEast",
					this.laserEast0, this.laserEast1, this.laserEast2,
					this.laserEast3);

			stack2.addButtons(this.laserEast0, this.laserEast1,
					this.laserEast2, this.laserEast3);
			stack2.setAutoHeight();
			stack2.setWidth100();

			ButtonStack stack3 = new ButtonStack(
					Page.props.mapGeneratorEditingWindow_south());
			this.laserSouth0 = new RadioToggleButton(
					"mapgenerator/laser_south_00.png", "SOUTH0");
			this.laserSouth1 = new RadioToggleButton(
					"mapgenerator/laser_south_01.png", "SOUTH1");
			this.laserSouth2 = new RadioToggleButton(
					"mapgenerator/laser_south_02.png", "SOUTH2");
			this.laserSouth3 = new RadioToggleButton(
					"mapgenerator/laser_south_03.png", "SOUTH3");

			RadioToggleButton.addButtonsToRadioGroup("LaserSouth",
					this.laserSouth0, this.laserSouth1, this.laserSouth2,
					this.laserSouth3);

			stack3.addButtons(this.laserSouth0, this.laserSouth1,
					this.laserSouth2, this.laserSouth3);
			stack3.setAutoHeight();
			stack3.setWidth100();

			ButtonStack stack4 = new ButtonStack(
					Page.props.mapGeneratorEditingWindow_west());
			this.laserWest0 = new RadioToggleButton(
					"mapgenerator/laser_west_00.png", "WEST0");
			this.laserWest1 = new RadioToggleButton(
					"mapgenerator/laser_west_01.png", "WEST1");
			this.laserWest2 = new RadioToggleButton(
					"mapgenerator/laser_west_02.png", "WEST2");
			this.laserWest3 = new RadioToggleButton(
					"mapgenerator/laser_west_03.png", "WEST3");

			RadioToggleButton.addButtonsToRadioGroup("LaserWest",
					this.laserWest0, this.laserWest1, this.laserWest2,
					this.laserWest3);

			stack4.addButtons(this.laserWest0, this.laserWest1,
					this.laserWest2, this.laserWest3);
			stack4.setAutoHeight();
			stack4.setWidth100();

			tempStack.addMembers(stack1, stack2, stack3, stack4);
			tempStack.setAutoHeight();
			this.laserSection.addItem(tempStack);
			this.menueStack.addSection(this.laserSection);
		}

		/**
		 * Stellt die buildPusherDirectionArea dar
		 * 
		 * @param expanded
		 */
		protected void buildPusherDirectionArea(boolean expanded) {
			this.pusherSection = new SectionStackSection(
					Page.props.mapGeneratorEditingWindow_Feldspezifiktion());
			this.pusherSection.setExpanded(expanded);

			VerticalStack tempStack = new VerticalStack(0, 0);
			ButtonStack stack1 = new ButtonStack(
					Page.props
							.mapGeneratorEditingWindow_buildPusherDirectionArea_buttonStack());

			this.pusherNorth = new RadioToggleButton(
					"mapgenerator/pusher_north.png", "N");
			this.pusherEast = new RadioToggleButton(
					"mapgenerator/pusher_east.png", "O");
			this.pusherSouth = new RadioToggleButton(
					"mapgenerator/pusher_south.png", "S");
			this.pusherWest = new RadioToggleButton(
					"mapgenerator/pusher_west.png", "W");

			RadioToggleButton.addButtonsToRadioGroup("PusherDirection",
					this.pusherNorth, this.pusherEast, this.pusherSouth,
					this.pusherWest);

			stack1.addButtons(this.pusherNorth, this.pusherEast,
					this.pusherSouth, this.pusherWest);
			stack1.setAutoHeight();
			stack1.setWidth100();

			ButtonStack stack2 = new ButtonStack(
					Page.props
							.mapGeneratorEditingWindow_buildPusherDirectionArea_active());

			this.pusherUneven = new RadioToggleButton(
					"mapgenerator/marker_enabled.png", "J");
			this.pusherAlways = new RadioToggleButton(
					"mapgenerator/marker_disabled.png", "N");

			RadioToggleButton.addButtonsToRadioGroup("PusherEvenOrUneven",
					this.pusherAlways, this.pusherUneven);

			stack2.addButtons(this.pusherUneven, this.pusherAlways);
			stack2.setAutoHeight();
			stack2.setWidth100();

			tempStack.addMembers(stack1, stack2);
			tempStack.setAutoHeight();
			this.pusherSection.addItem(tempStack);
			this.menueStack.addSection(this.pusherSection);
		}

		/**
		 * Stellt die buildRepairValueArea dar
		 * 
		 * @param expanded
		 */
		protected void buildRepairValueArea(boolean expanded) {
			this.repairFieldSection = new SectionStackSection(
					Page.props.mapGeneratorEditingWindow_Feldspezifiktion());
			this.repairFieldSection.setExpanded(expanded);

			VerticalStack tempStack = new VerticalStack(0, 0);
			ButtonStack stack = new ButtonStack(
					Page.props
							.mapGeneratorEditingWindow_repairSection_buttonStack());

			this.repairValue1 = new RadioToggleButton(
					"mapgenerator/repair_01.png", "1");
			this.repairValue2 = new RadioToggleButton(
					"mapgenerator/repair_02.png", "2");

			RadioToggleButton.addButtonsToRadioGroup("RepairValues",
					this.repairValue1, this.repairValue2);

			stack.addButtons(this.repairValue1, this.repairValue2);
			stack.setAutoHeight();
			stack.setWidth100();
			tempStack.addMember(stack);
			tempStack.setAutoHeight();
			this.repairFieldSection.addItem(tempStack);
			this.menueStack.addSection(this.repairFieldSection);
		}

		/**
		 * Stellt die StartNumberArea dar
		 * 
		 * @param expanded
		 */
		protected void buildStartNumberArea(boolean expanded) {
			this.startSection = new SectionStackSection(
					Page.props.mapGeneratorEditingWindow_Feldspezifiktion());
			this.startSection.setExpanded(expanded);

			VerticalStack tempStack = new VerticalStack(0, 0);
			VerticalStack stack1 = new VerticalStack(
					Page.props.mapGeneratorEditingWindow_StartNumber());
			ButtonStack s1 = new ButtonStack(Integer.valueOf(Page.props
					.global_marginInStackAreas()), 0);
			ButtonStack s2 = new ButtonStack(Integer.valueOf(Page.props
					.global_marginInStackAreas()), 0);
			ButtonStack stack2 = new ButtonStack(
					Page.props.mapGeneratorEditingWindow_StartDirection());

			this.start1 = new RadioToggleButton(
					"mapgenerator/startfield_01.png", "1");
			this.start2 = new RadioToggleButton(
					"mapgenerator/startfield_02.png", "2");
			this.start3 = new RadioToggleButton(
					"mapgenerator/startfield_03.png", "3");
			this.start4 = new RadioToggleButton(
					"mapgenerator/startfield_04.png", "4");
			this.start5 = new RadioToggleButton(
					"mapgenerator/startfield_05.png", "5");
			this.start6 = new RadioToggleButton(
					"mapgenerator/startfield_06.png", "6");
			this.start7 = new RadioToggleButton(
					"mapgenerator/startfield_07.png", "7");
			this.start8 = new RadioToggleButton(
					"mapgenerator/startfield_08.png", "8");
			this.start9 = new RadioToggleButton(
					"mapgenerator/startfield_09.png", "9");
			this.start10 = new RadioToggleButton(
					"mapgenerator/startfield_10.png", "10");
			this.start11 = new RadioToggleButton(
					"mapgenerator/startfield_11.png", "11");
			this.start12 = new RadioToggleButton(
					"mapgenerator/startfield_12.png", "12");

			RadioToggleButton.addButtonsToRadioGroup("StartNumbers",
					this.start1, this.start2, this.start3, this.start4,
					this.start5, this.start6, this.start7, this.start8,
					this.start9, this.start10, this.start11, this.start12);

			s1.addButtons(this.start1, this.start2, this.start3, this.start4,
					this.start5, this.start6);
			s1.setAutoHeight();
			s1.setAutoWidth();
			s2.addButtons(this.start7, this.start8, this.start9, this.start10,
					this.start11, this.start12);
			s2.setAutoHeight();
			s2.setAutoWidth();
			stack1.addMembers(s1, s2);
			stack1.setAutoHeight();
			stack1.setWidth100();

			
			this.restartNorth = new RadioToggleButton(
					"mapgenerator/arrow_north.png", "NORTH");
			this.restartEast = new RadioToggleButton(
					"mapgenerator/arrow_east.png", "EAST");
			this.restartSouth = new RadioToggleButton(
					"mapgenerator/arrow_south.png", "SOUTH");
			this.restartWest = new RadioToggleButton(
					"mapgenerator/arrow_west.png", "WEST");

			RadioToggleButton.addButtonsToRadioGroup("RestartGroup",
					this.restartNorth, this.restartEast, this.restartSouth,
					this.restartWest);

			stack2.addButtons(this.restartNorth, this.restartEast,
					this.restartSouth, this.restartWest);
			stack2.setAutoHeight();
			stack2.setWidth100();

			tempStack.addMembers(stack1, stack2);
			tempStack.setAutoHeight();
			this.startSection.addItem(tempStack);
			this.menueStack.addSection(this.startSection);
		}

		/**
		 * Stellt die WallArea dar
		 * 
		 * @param expanded
		 */
		protected void buildWallArea(boolean expanded) {
			this.wallSection = new SectionStackSection(
					Page.props.mapGeneratorEditingWindow_Waende());
			this.wallSection.setExpanded(expanded);

			VerticalStack tempStack = new VerticalStack(0, 0);
			ButtonStack stack = new ButtonStack(
					Page.props.mapGeneratorEditingWindow_Waende());

			this.wallNorth = new ToggleButton("mapgenerator/wall_north.png",
					"N");
			this.wallEast = new ToggleButton("mapgenerator/wall_east.png", "O");
			this.wallSouth = new ToggleButton("mapgenerator/wall_south.png",
					"S");
			this.wallWest = new ToggleButton("mapgenerator/wall_west.png", "W");

			stack.addButtons(this.wallNorth, this.wallEast, this.wallSouth,
					this.wallWest);
			stack.setAutoHeight();
			stack.setWidth100();
			tempStack.addMember(stack);
			tempStack.setAutoHeight();
			this.wallSection.addItem(tempStack);
			this.menueStack.addSection(this.wallSection);
		}

		/**
		 * Liefert check1
		 * 
		 * @return check1
		 */
		public RadioToggleButton getCheck1() {
			return this.check1;
		}

		/**
		 * Liefert check2
		 * 
		 * @return check2
		 */
		public RadioToggleButton getCheck2() {
			return this.check2;
		}

		/**
		 * Liefert check3
		 * 
		 * @return check3
		 */
		public RadioToggleButton getCheck3() {
			return this.check3;
		}

		/**
		 * Liefert check4
		 * 
		 * @return check4
		 */
		public RadioToggleButton getCheck4() {
			return this.check4;
		}

		/**
		 * Liefert check5
		 * 
		 * @return check5
		 */
		public RadioToggleButton getCheck5() {
			return this.check5;
		}

		/**
		 * Liefert check6
		 * 
		 * @return check6
		 */
		public RadioToggleButton getCheck6() {
			return this.check6;
		}

		/**
		 * Liefert check7
		 * 
		 * @return check7
		 */
		public RadioToggleButton getCheck7() {
			return this.check7;
		}

		/**
		 * Liefert check8
		 * 
		 * @return check8
		 */
		public RadioToggleButton getCheck8() {
			return this.check8;
		}

		/**
		 * Liefert checkpointSection
		 * 
		 * @return checkpointSection
		 */
		public SectionStackSection getCheckpointSection() {
			return this.checkpointSection;
		}

		/**
		 * Liefert compactorAlways
		 * 
		 * @return compactorAlways
		 */
		public RadioToggleButton getCompactorAlways() {
			return this.compactorAlways;
		}

		/**
		 * Liefert compactorEast
		 * 
		 * @return compactorEast
		 */
		public RadioToggleButton getCompactorEast() {
			return this.compactorEast;
		}

		/**
		 * Liefert compactorFirstLast
		 * 
		 * @return compactorFirstLast
		 */
		public RadioToggleButton getCompactorFirstLast() {
			return this.compactorFirstLast;
		}

		/**
		 * Liefert compactorNorth
		 * 
		 * @return compactorNorth
		 */
		public RadioToggleButton getCompactorNorth() {
			return this.compactorNorth;
		}

		/**
		 * Liefert compactorSection
		 * 
		 * @return compactorSection
		 */
		public SectionStackSection getCompactorSection() {
			return this.compactorSection;
		}

		/**
		 * Liefert compactorSouth
		 * 
		 * @return compactorSouth
		 */
		public RadioToggleButton getCompactorSouth() {
			return this.compactorSouth;
		}

		/**
		 * Liefert compactorWest
		 * 
		 * @return compactorWest
		 */
		public RadioToggleButton getCompactorWest() {
			return this.compactorWest;
		}

		/**
		 * Liefert rotateConveyorBeltSouthEast
		 * 
		 * @return rotateConveyorBeltSouthEast
		 */
		public RadioToggleButton getConveyorBeltEast() {
			return this.conveyorBeltEast;
		}

		public ToggleButton getConveyorBeltInEast() {
			return this.conveyorBeltInEast;
		}

		public ToggleButton getConveyorBeltInNorth() {
			return this.conveyorBeltInNorth;
		}

		public ToggleButton getConveyorBeltInSouth() {
			return this.conveyorBeltInSouth;
		}

		public ToggleButton getConveyorBeltInWest() {
			return this.conveyorBeltInWest;
		}

		/**
		 * Liefert rotateConveyorBeltNorthWest
		 * 
		 * @return rotateConveyorBeltNorthWest
		 */
		public RadioToggleButton getConveyorBeltNorth() {
			return this.conveyorBeltNorth;
		}

		/**
		 * Liefert straightConveyorBeltValue1
		 * 
		 * @return straightConveyorBeltValue1
		 */
		public RadioToggleButton getConveyorBeltRange1() {
			return this.conveyorBeltRange1;
		}

		/**
		 * Liefert straightConveyorBeltValue2
		 * 
		 * @return straightConveyorBeltValue2
		 */
		public RadioToggleButton getConveyorBeltRange2() {
			return this.conveyorBeltRange2;
		}

		/**
		 * Liefert rotateConveyorBeltEastSouth
		 * 
		 * @return rotateConveyorBeltEastSouth
		 */
		public RadioToggleButton getConveyorBeltSouth() {
			return this.conveyorBeltSouth;
		}

		/**
		 * Liefert rotateConveyorBeltSouthWest
		 * 
		 * @return rotateConveyorBeltSouthWest
		 */
		public RadioToggleButton getConveyorBeltWest() {
			return this.conveyorBeltWest;
		}

		/**
		 * Liefert gearLeft
		 * 
		 * @return gearLeft
		 */
		public RadioToggleButton getGearLeft() {
			return this.gearLeft;
		}

		/**
		 * Liefert gearRight
		 * 
		 * @return gearRight
		 */
		public RadioToggleButton getGearRight() {
			return this.gearRight;
		}

		/**
		 * Liefert gearSection
		 * 
		 * @return gearSection
		 */
		public SectionStackSection getGearSection() {
			return this.gearSection;
		}

		/**
		 * Liefert laserEast0
		 * 
		 * @return laserEast0
		 */
		public RadioToggleButton getLaserEast0() {
			return this.laserEast0;
		}

		/**
		 * Liefert laserEast1
		 * 
		 * @return laserEast1
		 */
		public RadioToggleButton getLaserEast1() {
			return this.laserEast1;
		}

		/**
		 * Liefert laserEast2
		 * 
		 * @return laserEast2
		 */
		public RadioToggleButton getLaserEast2() {
			return this.laserEast2;
		}

		/**
		 * Liefert laserEast3
		 * 
		 * @return laserEast3
		 */
		public RadioToggleButton getLaserEast3() {
			return this.laserEast3;
		}

		/**
		 * Liefert laserNorth0
		 * 
		 * @return laserNorth0
		 */
		public RadioToggleButton getLaserNorth0() {
			return this.laserNorth0;
		}

		/**
		 * Liefert laserNorth1
		 * 
		 * @return laserNorth1
		 */
		public RadioToggleButton getLaserNorth1() {
			return this.laserNorth1;
		}

		/**
		 * Liefert laserNorth2
		 * 
		 * @return laserNorth2
		 */
		public RadioToggleButton getLaserNorth2() {
			return this.laserNorth2;
		}

		/**
		 * Liefert laserNorth3
		 * 
		 * @return laserNorth3
		 */
		public RadioToggleButton getLaserNorth3() {
			return this.laserNorth3;
		}

		/**
		 * Liefert laserSection
		 * 
		 * @return laserSection
		 */
		public SectionStackSection getLaserSection() {
			return this.laserSection;
		}

		/**
		 * Liefert laserSouth0
		 * 
		 * @return laserSouth0
		 */
		public RadioToggleButton getLaserSouth0() {
			return this.laserSouth0;
		}

		/**
		 * Liefert laserSouth1
		 * 
		 * @return laserSouth1
		 */
		public RadioToggleButton getLaserSouth1() {
			return this.laserSouth1;
		}

		/**
		 * Liefert laserSouth2
		 * 
		 * @return laserSouth2
		 */
		public RadioToggleButton getLaserSouth2() {
			return this.laserSouth2;
		}

		/**
		 * Liefert laserSouth3
		 * 
		 * @return laserSouth3
		 */
		public RadioToggleButton getLaserSouth3() {
			return this.laserSouth3;
		}

		/**
		 * Liefert laserWest0
		 * 
		 * @return laserWest0
		 */
		public RadioToggleButton getLaserWest0() {
			return this.laserWest0;
		}

		/**
		 * Liefert laserWest1
		 * 
		 * @return laserWest1
		 */
		public RadioToggleButton getLaserWest1() {
			return this.laserWest1;
		}

		/**
		 * Liefert laserWest2
		 * 
		 * @return laserWest2
		 */
		public RadioToggleButton getLaserWest2() {
			return this.laserWest2;
		}

		/**
		 * Liefert laserWest3
		 * 
		 * @return laserWest3
		 */
		public RadioToggleButton getLaserWest3() {
			return this.laserWest3;
		}

		/**
		 * Liefert menueStack
		 * 
		 * @return menueStack
		 */
		public SectionStack getMenueStack() {
			return this.menueStack;
		}

		/**
		 * Liefert pusherAlways
		 * 
		 * @return pusherAlways
		 */
		public RadioToggleButton getPusherAlways() {
			return this.pusherAlways;
		}

		/**
		 * Liefert pusherEast
		 * 
		 * @return pusherEast
		 */
		public RadioToggleButton getPusherEast() {
			return this.pusherEast;
		}

		/**
		 * Liefert pusherNorth
		 * 
		 * @return pusherNorth
		 */
		public RadioToggleButton getPusherNorth() {
			return this.pusherNorth;
		}

		/**
		 * Liefert pusherSection
		 * 
		 * @return pusherSection
		 */
		public SectionStackSection getPusherSection() {
			return this.pusherSection;
		}

		/**
		 * Liefert pusherSouth
		 * 
		 * @return pusherSouth
		 */
		public RadioToggleButton getPusherSouth() {
			return this.pusherSouth;
		}

		/**
		 * Liefert pusherUneven
		 * 
		 * @return pusherUneven
		 */
		public RadioToggleButton getPusherUneven() {
			return this.pusherUneven;
		}

		/**
		 * Liefert pusherWest
		 * 
		 * @return pusherWest
		 */
		public RadioToggleButton getPusherWest() {
			return this.pusherWest;
		}

		/**
		 * Liefert repairFieldSection
		 * 
		 * @return repairFieldSection
		 */
		public SectionStackSection getRepairFieldSection() {
			return this.repairFieldSection;
		}

		/**
		 * Liefert repairValue1
		 * 
		 * @return repairValue1
		 */
		public RadioToggleButton getRepairValue1() {
			return this.repairValue1;
		}

		/**
		 * Liefert repairValue2
		 * 
		 * @return repairValue2
		 */
		public RadioToggleButton getRepairValue2() {
			return this.repairValue2;
		}

		/**
		 * @return the restartEast
		 */
		public RadioToggleButton getRestartEast() {
			return this.restartEast;
		}

		/**
		 * @return the restartNorth
		 */
		public RadioToggleButton getRestartNorth() {
			return this.restartNorth;
		}

		/**
		 * @return the restartSouth
		 */
		public RadioToggleButton getRestartSouth() {
			return this.restartSouth;
		}

		/**
		 * @return the restartWest
		 */
		public RadioToggleButton getRestartWest() {
			return this.restartWest;
		}

		/**
		 * Liefert die rotateConveyorBeltSection
		 * 
		 * @return rotateConveyorBeltSection
		 */
		public SectionStackSection getRotateConveyorBeltSection() {
			return this.conveyorBeltSection;
		}

		/**
		 * Liefert start1
		 * 
		 * @return start1
		 */
		public RadioToggleButton getStart1() {
			return this.start1;
		}

		/**
		 * Liefert start10
		 * 
		 * @return start10
		 */
		public RadioToggleButton getStart10() {
			return this.start10;
		}

		/**
		 * Liefert start11
		 * 
		 * @return start11
		 */
		public RadioToggleButton getStart11() {
			return this.start11;
		}

		/**
		 * Liefert start12
		 * 
		 * @return start12
		 */
		public RadioToggleButton getStart12() {
			return this.start12;
		}

		/**
		 * Liefert start2
		 * 
		 * @return start2
		 */
		public RadioToggleButton getStart2() {
			return this.start2;
		}

		/**
		 * Liefert start3
		 * 
		 * @return start3
		 */
		public RadioToggleButton getStart3() {
			return this.start3;
		}

		/**
		 * Liefert start4
		 * 
		 * @return start4
		 */
		public RadioToggleButton getStart4() {
			return this.start4;
		}

		/**
		 * Liefert start5
		 * 
		 * @return start5
		 */
		public RadioToggleButton getStart5() {
			return this.start5;
		}

		/**
		 * Liefert start6
		 * 
		 * @return start6
		 */
		public RadioToggleButton getStart6() {
			return this.start6;
		}

		/**
		 * Liefert start7
		 * 
		 * @return start7
		 */
		public RadioToggleButton getStart7() {
			return this.start7;
		}

		/**
		 * Liefert start8
		 * 
		 * @return start8
		 */
		public RadioToggleButton getStart8() {
			return this.start8;
		}

		/**
		 * Liefert start9
		 * 
		 * @return start9
		 */
		public RadioToggleButton getStart9() {
			return this.start9;
		}

		/**
		 * Liefert startSection
		 * 
		 * @return startSection
		 */
		public SectionStackSection getStartSection() {
			return this.startSection;
		}


		/**
		 * Liefert wallEast
		 * 
		 * @return wallEast
		 */
		public ToggleButton getWallEast() {
			return this.wallEast;
		}

		/**
		 * Liefert wallNorth
		 * 
		 * @return wallNorth
		 */
		public ToggleButton getWallNorth() {
			return this.wallNorth;
		}

		/**
		 * Liefert wallSection
		 * 
		 * @return wallSection
		 */
		public SectionStackSection getWallSection() {
			return this.wallSection;
		}

		/**
		 * Liefert wallSouth
		 * 
		 * @return wallSouth
		 */
		public ToggleButton getWallSouth() {
			return this.wallSouth;
		}

		/**
		 * Liefert wallWest
		 * 
		 * @return wallWest
		 */
		public ToggleButton getWallWest() {
			return this.wallWest;
		}

	}

	// Attribute
	final private SelectItem fieldTypeBox;
	final private LinkedHashMap<String, String> fieldTypeMap;
	final private BasicFieldStack mainAreaBasic;
	final private CheckpointFieldStack mainAreaCheckpoint;
	final private CompactorFieldStack mainAreaCompactor;
	final private VerticalStack mainAreaEmpty, buttonArea, mainSectionArea;
	final private GearFieldStack mainAreaGear;
	final private HoleFieldStack mainAreaHole;
	final private PusherFieldStack mainAreaPusher;

	final private RepairFieldStack mainAreaRepair;

	final private ConveyorBeltFieldStack mainAreaConveyorBelt;

	final private StartFieldStack mainAreaStart;

	final private StandardLabel pleaseChooseLabel;

	final private Form selectForm;

	final private SectionStack sectionStack;

	final private SectionStackSection menueButtonSection, menueSection,
			miniMapSection, mainSection;

	final private Layout miniMapArea;

	final private Button buttonCreate, buttonLoad, buttonUnload, buttonSave, buttonMenue, buttonDelete, buttonTransform;

	final private CheckboxItem checkBox_showWays;

	/**
	 * Konstruktor
	 */
	public MapGeneratorEditWindow() {
		setTitle(Page.props.mapGeneratorEditingWindow_title());
		this.setWidth(252);
		this.setHeight(525);
		setCanDragReposition(false);
		setCanDragResize(false);
		setKeepInParentRect(true);
		setOverflow(Overflow.AUTO);

		this.mainAreaEmpty = new VerticalStack(Integer.valueOf(Page.props
				.global_marginBetweenStackAreas()), Integer.valueOf(Page.props
				.global_paddingInStackAreas()));
		this.pleaseChooseLabel = new StandardLabel();

		this.mainAreaBasic = new BasicFieldStack();
		this.mainAreaStart = new StartFieldStack();
		this.mainAreaCheckpoint = new CheckpointFieldStack();
		this.mainAreaGear = new GearFieldStack();
		this.mainAreaHole = new HoleFieldStack();
		this.mainAreaPusher = new PusherFieldStack();
		this.mainAreaCompactor = new CompactorFieldStack();
		this.mainAreaRepair = new RepairFieldStack();
		this.mainAreaConveyorBelt = new ConveyorBeltFieldStack();

		this.fieldTypeMap = new LinkedHashMap<String, String>();
		this.fieldTypeMap.put("BasicField",
				Page.props.mapGeneratorEditingWindow_BasicField());
		this.fieldTypeMap.put("HoleField",
				Page.props.mapGeneratorEditingWindow_HoleField());
		this.fieldTypeMap.put("StartField",
				Page.props.mapGeneratorEditingWindow_StartField());
		this.fieldTypeMap.put("RepairField",
				Page.props.mapGeneratorEditingWindow_RepairField());
		this.fieldTypeMap.put("CheckpointField",
				Page.props.mapGeneratorEditingWindow_CheckpointField());
		this.fieldTypeMap.put("GearField",
				Page.props.mapGeneratorEditingWindow_GearField());
		this.fieldTypeMap.put("PusherField",
				Page.props.mapGeneratorEditingWindow_PusherField());
		this.fieldTypeMap.put("CompactorField",
				Page.props.mapGeneratorEditingWindow_CompactorField());
		this.fieldTypeMap.put("ConveyorBeltField",
				Page.props.mapGeneratorEditingWindow_ConveyorBeltField());

		this.fieldTypeBox = new SelectItem();
		this.fieldTypeBox.setTitle(Page.props
				.mapGeneratorEditingWindow_fieldTypeBox());
		this.fieldTypeBox.setShowTitle(true);
		this.fieldTypeBox.setHeight(19);
		this.fieldTypeBox.setWidth(100);
		this.fieldTypeBox.setValueMap(this.fieldTypeMap);
		this.fieldTypeBox.setDisabled(true);

		this.selectForm = new Form();
		this.selectForm.setCellPadding(-5);
		this.selectForm.setNumCols(1);
		this.selectForm.setLayoutAlign(Alignment.CENTER);
		this.selectForm.setItems(this.fieldTypeBox);

		setHeaderControls(HeaderControls.HEADER_LABEL, this.selectForm);

		this.mainAreaEmpty.addMember(this.pleaseChooseLabel);

		this.buttonArea = new VerticalStack(Integer.valueOf(Page.props
				.global_marginInStackAreas()), Integer.valueOf(Page.props
				.global_paddingInStackAreas()));
		this.buttonArea.setHeight100();
		this.buttonCreate = new Button(
				Page.props.mapGeneratorEditingWindow_createButton(), 219);
		this.buttonSave = new Button(
				Page.props.mapGeneratorEditingWindow_saveButton(), 219);
		this.buttonSave.setDisabled(true);
		this.buttonLoad = new Button(
				Page.props.mapGeneratorEditingWindow_loadButton(), 219);
		this.buttonUnload = new Button(
				Page.props.mapGeneratorEditingWindow_unloadButton(), 219);
		this.buttonUnload.setDisabled(true);
		this.buttonDelete = new Button(
				Page.props.mapGeneratorEditingWindow_deleteButton(), 219);
		this.buttonTransform = new Button(
				Page.props.mapGeneratorEditingWindow_transformButton(), 219);
		this.buttonTransform.setDisabled(true);
		
		this.buttonArea.addMembers(this.buttonLoad, this.buttonDelete, this.buttonCreate, this.buttonSave,
				this.buttonUnload, this.buttonTransform);

		this.sectionStack = new SectionStack();
		this.sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		this.sectionStack.setCanResizeSections(false);
		this.buttonMenue = new Button(
				Page.props.mapGeneratorEditingWindow_menueOff());
		this.buttonMenue.setWidth100();
		this.buttonMenue.setDisabled(true);
		this.menueButtonSection = new SectionStackSection();
		this.menueButtonSection.setExpanded(true);
		this.menueButtonSection.setShowHeader(false);
		this.menueButtonSection.addItem(this.buttonMenue);
		this.menueSection = new SectionStackSection();
		this.menueSection.setExpanded(true);
		this.menueSection.setShowHeader(false);
		this.menueSection.addItem(this.buttonArea);

		final DynamicForm dForm = new DynamicForm();
		this.checkBox_showWays = new CheckboxItem();
		this.checkBox_showWays.setTitle(Page.props
				.mapGeneratorEditingWindow_optimalWays());
		dForm.setFields(this.checkBox_showWays);
		this.menueSection.addItem(dForm);

		this.miniMapArea = new Layout();
		this.miniMapArea.setStyleName("miniMap");
		this.miniMapSection = new SectionStackSection(
				Page.props.mapGeneratorMinimapWindow_title());
		this.miniMapSection.setExpanded(false);
		this.miniMapSection.addItem(this.miniMapArea);
		this.miniMapArea.setAlign(Alignment.CENTER);
		this.mainSectionArea = new VerticalStack();
		this.mainSectionArea.addMembers(this.mainAreaEmpty, // ID: 0
				this.mainAreaBasic, // ID: 1
				this.mainAreaStart, // ID: 2
				this.mainAreaCheckpoint, // ID: 3
				this.mainAreaGear, // ID: 4
				this.mainAreaHole, // ID: 5
				this.mainAreaPusher, // ID: 6
				this.mainAreaCompactor, // ID: 7
				this.mainAreaRepair, // ID: 8
				this.mainAreaConveyorBelt); // ID: 9
		this.mainSectionArea.setHeight(300);
		this.mainSection = new SectionStackSection();
		this.mainSection.setExpanded(false);
		this.mainSection.setShowHeader(false);
		this.mainSection.setItems(this.mainSectionArea);
		// siehe: MapGeneratorPresenter

		// Items 1-9: hide()
		for (int i = 1; i < 10; i++) {
			this.mainSectionArea.getMembers()[i].hide();
		}

		this.sectionStack.addSection(this.menueButtonSection);
		this.sectionStack.addSection(this.menueSection);
		this.sectionStack.addSection(this.miniMapSection);
		this.sectionStack.addSection(this.mainSection);
		addItem(this.sectionStack);
	}

	/**
	 * Liefert den buttonCreate
	 * 
	 * @return buttonCreate
	 */
	public Button getButtonCreate() {
		return this.buttonCreate;
	}

	/**
	 * Liefert den buttonLoad
	 * 
	 * @return buttonLoad
	 */
	public Button getButtonLoad() {
		return this.buttonLoad;
	}
	
	/**
	 * Liefert den buttonUnload
	 * 
	 * @return buttonLoad
	 */
	public Button getButtonUnload() {
		return this.buttonUnload;
	}

	/**
	 * Liefert den buttonMenue
	 * 
	 * @return buttonMenue
	 */
	public Button getButtonMenue() {
		return this.buttonMenue;
	}

	/**
	 * Liefert den buttonSave
	 * 
	 * @return buttonSave
	 */
	public Button getButtonSave() {
		return this.buttonSave;
	}

	public CheckboxItem getCheckBox_showWays() {
		return this.checkBox_showWays;
	}

	/**
	 * Liefert die fieldTypeBox (Auswahl des Feldtypes in Windowheader)
	 * 
	 * @return fieldTypeBox
	 */
	public FormItem getFieldTypeBox() {
		return this.fieldTypeBox;
	}

	/**
	 * Liefert die linkedHashMap (Feldtypen)
	 * 
	 * @return linkedHashMap
	 */
	public LinkedHashMap<String, String> gethMap() {
		return this.fieldTypeMap;
	}

	/**
	 * Liefert die mainAreaBasic (Basic Field)
	 * 
	 * @return mainAreaBasic
	 */
	public BasicFieldStack getMainAreaBasic() {
		return this.mainAreaBasic;
	}

	/**
	 * Liefert die mainAreaCheckpoint (Checkpoint Field)
	 * 
	 * @return mainAreaCheckpoint
	 */
	public CheckpointFieldStack getMainAreaCheckpoint() {
		return this.mainAreaCheckpoint;
	}

	/**
	 * Liefert die mainAreaCompactor (Compactor Field)
	 * 
	 * @return mainAreaCompactor
	 */
	public CompactorFieldStack getMainAreaCompactor() {
		return this.mainAreaCompactor;
	}

	/**
	 * Liefert die mainAreaEmpty (leer)
	 * 
	 * @return mainAreaEmpty
	 */
	public VerticalStack getMainAreaEmpty() {
		return this.mainAreaEmpty;
	}

	/**
	 * Liefert die mainAreaGear (Gear Field)
	 * 
	 * @return mainAreaGear
	 */
	public GearFieldStack getMainAreaGear() {
		return this.mainAreaGear;
	}

	/**
	 * Liefert die mainAreaHole (Hole Field)
	 * 
	 * @return mainAreaHole
	 */
	public HoleFieldStack getMainAreaHole() {
		return this.mainAreaHole;
	}

	/**
	 * Liefert die mainAreaPusher (Pusher Field)
	 * 
	 * @return mainAreaPusher
	 */
	public PusherFieldStack getMainAreaPusher() {
		return this.mainAreaPusher;
	}

	/**
	 * Liefert die getMainAreaRepair (Repair Field)
	 * 
	 * @return getMainAreaRepair
	 */
	public RepairFieldStack getMainAreaRepair() {
		return this.mainAreaRepair;
	}

	/**
	 * Liefert die mainAreaStart (Start Field)
	 * 
	 * @return mainAreaStart
	 */
	public StartFieldStack getMainAreaStart() {
		return this.mainAreaStart;
	}

	/**
	 * Liefert die mainAreaRotateConveyorBelt (Fließbandkurve)
	 * 
	 * @returnmainAreaRopateConveyorBelt
	 */
	public ConveyorBeltFieldStack getMainConveyorBelt() {
		return this.mainAreaConveyorBelt;
	}

	/**
	 * Liefert die mainSection
	 * 
	 * @return mainSection
	 */
	public SectionStackSection getMainSection() {
		return this.mainSection;
	}

	/**
	 * Liefert die mainSectionArea (VerticalStack in mainSection)
	 * 
	 * @return mainSectionArea
	 */
	public VerticalStack getMainSectionArea() {
		return this.mainSectionArea;
	}

	/**
	 * Liefert die menueSection
	 * 
	 * @return menueSection
	 */
	public SectionStackSection getMenueSection() {
		return this.menueButtonSection;
	}

	/**
	 * Liefert die miniMapSection
	 * 
	 * @return miniMapSection
	 */
	public SectionStackSection getMiniMapSection() {
		return this.miniMapSection;
	}

	/**
	 * Liefert den sectionStack
	 * 
	 * @return sectionStack
	 */
	public SectionStack getSectionStack() {
		return this.sectionStack;
	}

	public Form getSelectForm() {
		return this.selectForm;
	}

	/**
	 * Setzt die miniMap in die miniMapSection
	 * 
	 * @param miniMap
	 *            miniMap
	 * @return true
	 */
	public boolean setMiniMap(final Minimap miniMap, final VStack handCursorLayout) {
		this.miniMapArea.removeMembers(this.miniMapArea.getMembers());
		this.miniMapArea.setHeight(miniMap.getHeight());
		this.miniMapArea.setWidth100();
		
		final VStack inner = new VStack();
		inner.setWidth(miniMap.getWidth());
		inner.setHeight(miniMap.getHeight());
		inner.setBackgroundColor("#636363");
		inner.setBorder("1px solid yellow");
		inner.addChild(miniMap);
		
		inner.addChild(handCursorLayout);
		
		this.miniMapArea.addMember(inner);

		return true;
	}

	public Layout getMiniMapArea() {
		return miniMapArea;
	}

	public Button getButtonDelete() {
		return buttonDelete;
	}

	public Button getButtonTransform() {
		return buttonTransform;
	}
	
	
}
