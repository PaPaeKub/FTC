package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Utilize.WrapRads;
import static org.firstinspires.ftc.teamcode.Utilize.toRadian;
import static org.firstinspires.ftc.teamcode.Utilize.toDegree;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Set;

@TeleOp(name="Tele")
public class Tele extends Robot {

    private Controller controller;

    // Variables
    int Level, CurrentPosition = 0;
    double setpoint = 0, H_Ang = 0, AL_Ang = 0, AD_Ang = 0, ArmPos = 0, GripPos = 0;
    boolean ls = false, On = false, ag = false, press_ag = false, press = false, Lift_isAuto = false, LB_Press = false, RB_Press = false,
            r_disable = false, Left_isTouch = false, Right_isTouch = false, push = false, press_sq = false, pe = false;
    double CurrentTime = System.nanoTime() * 1E-9,  lastRXtime = CurrentTime;
    private void Init() {
        // Initialize Robot
        Initialize(DcMotor.RunMode.RUN_WITHOUT_ENCODER, new double[]{0, 0, AL_Ang},
                                                        new double[]{0, 0, 0, 0});

        controller = new Controller(1.2, 0.005, 0.1, 0 , 0.15, toRadian(0.75));
        SetServoPos(0, G, AG);
        SetServoPos(0, LA, RA);
        SetServoPos(0, LFA, RFA);
        SetServoPos(0, LAG, RAG);
        SetServoPos(0, LLG, RLG);
        SetServoPos(0.25, D);

        setpoint = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    private void Movement() {
        CurrentTime    = System.nanoTime() * 1E-9;
        double lift    = Math.max(LL.getCurrentPosition(), RL.getCurrentPosition());
        double speed   = 0.275;
        double lx      = gamepad1.left_stick_x;
        double ly      = -gamepad1.left_stick_y;
        double x1      = gamepad1.dpad_left ? -speed : gamepad1.dpad_right ? speed : lx;
        double y1      = gamepad1.dpad_up ? speed : gamepad1.dpad_down ? -speed : ly;
        double yaw     = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double heading = -yaw;
        double x2 = (Math.cos(heading) * x1) - (Math.sin(heading) * y1);
        double y2 = (Math.sin(heading) * x1) + (Math.cos(heading) * y1);
        // Rotate
        double r = r_disable ? 0 : controller.Calculate(WrapRads(yaw - setpoint));
        double x = gamepad1.right_stick_x;
        if (x != 0 || CurrentTime - lastRXtime < 0.45) {
            r = x;
            setpoint = yaw;
        }
        if (lx == 0 && ly == 0 && x== 0 && Math.abs(r) < 0.2)  r = 0;
        lastRXtime = x != 0 ? CurrentTime : lastRXtime;
        // Denominator for division to get no more than 1
        double d = Math.max(Math.abs(x2) + Math.abs(y2) + Math.abs(r), 1);
        // Lift limit speed
        double l = lift > 4000 ? 0.4 : lift > 2000 ? 0.7 : ls ? 0.4 : 1;

        MovePower((y2 + x2 + r) / d * l, (y2 - x2 - r) / d * l,
                  (y2 - x2 + r) / d * l, (y2 + x2 - r) / d * l);
        telemetry.addData("yaw", toDegree(yaw));
        telemetry.addData("setpoint", toDegree(setpoint));
//        telemetry.addData("error", controller.Error);
    }

    private void Lift() {
        double LT = gamepad2.left_trigger;
        double RT = gamepad2.right_trigger;
        double LPos = LL.getCurrentPosition();
        double RPos = RL.getCurrentPosition();
        Left_isTouch  = LTS.isPressed();
        Right_isTouch = RTS.isPressed();

        if (Left_isTouch)  LL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        if (Right_isTouch) RL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if (Left_isTouch || Right_isTouch && RT > 0) {
            LL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            RL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        double sp = LT > 0 ? -LT : RT > 0 ? RT : 0;
        double spL = Left_isTouch  && LT > 0 ? 0 : LPos > 4100 && RT > 0 ? 0 : sp;
        double spR = Right_isTouch && LT > 0 ? 0 : RPos > 4100 && RT > 0 ? 0 : sp;
        Lift_SetPower(spL, spR);
    }

    private void FrontArm() {
        LB_Press = gamepad2.left_bumper;
        RB_Press = gamepad2.right_bumper;

        if (LB_Press) {
            ArmPos = ArmPos - 0.01;
        }

        if (RB_Press) {
            ArmPos = ArmPos + 0.01;
        }

        ArmPos = Range.clip(ArmPos, 0, 1);
        SetServoPos(ArmPos, D);
        telemetry.addData("Arm", ArmPos);

        boolean tp = gamepad1.cross;
        boolean lb = gamepad1.left_bumper;
        boolean rb = gamepad1.right_bumper;
        double  lt = gamepad1.left_trigger;
        double  rt = gamepad1.right_trigger;

        if (lt > 0.5) {
            ArmPos = ArmPos - 0.025;
            ArmPos = Range.clip(ArmPos, 0, 0.5);
            SetServoPos(ArmPos, LA, RA);
        }
        if (rt > 0.5) {
            ArmPos = 0.5;
            SetServoPos(0.5, LA, RA);
            SetServoPos(0.2, G);
            SetServoPos(0.15, LAG, RAG);
            SetServoPos(0.6, LFA, RFA);
            On = true;
            ls = true;
        }

        if (lb && On) SetServoPos(0.6, LFA, RFA);
        if (rb && On) SetServoPos(0.55, LFA, RFA);

        if (!(tp)) {
            press = false;
            return;
        }
        if (press) return;
        press = true;
        if (!On) {
            SetServoPos(0.2, G);
            SetServoPos(0.15, LAG, RAG);
            SetServoPos(0.6, LFA, RFA);
            On = true;
            ls = true;
            return;
        }
        Sleep();
        SetServoPos(0, G);
        sleep(200);
        SetServoPos(1, LAG, RAG);
        SetServoPos(0, LA, RA);
        SetServoPos(0, LFA, RFA);
        SetServoPos(0, AG);
        ArmPos = 0;
        On = false;
        ls = false;
        press_ag = false;
        ag = false;
    }

    private void AdjustGripper() {
        boolean cr = gamepad1.triangle;

        if (!(cr)) {
            press_ag = false;
            return;
        }
        if (press_ag) return;
        press_ag = true;
        if (!ag) {
            SetServoPos(0.32, AG);
            ag = true;
            return;
        }
        SetServoPos(0, AG);
        ag = false;
    }

    private void PlaceElement() {
        boolean sq = gamepad1.square;
        if (sq) {
            Sleep();
            SetServoPos(0.5, LA, RA);
            SetServoPos(0.18, LAG, RAG);
            SetServoPos(0.76, LFA, RFA);
            sleep(500);
            SetServoPos(0.2, G);
            sleep(300);
            SetServoPos(0, LA, RA);
            SetServoPos(0, LAG, RAG);
            SetServoPos(0, LFA, RFA);
            SetServoPos(0, G);
            ls = false;
        }
    }

    @Override
    public void runOpMode() {
        Init();
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                Odomentry();
                Movement();
                Lift();
                FrontArm();
                AdjustGripper();
                PlaceElement();
//                telemetry.addData("XYH", "%6f cm %6f cm", Posx, Posy);
//                telemetry.addData("Lift", "%d", CurrentPosition);
//                telemetry.addData("LeftLift", "%d", LL.getCurrentPosition());
//                telemetry.addData("RightLift", "%d", RL.getCurrentPosition());
//                telemetry.addData("LRM", "%6d  %6d %6d", left_encoder_pos, right_encoder_pos, center_encoder_pos);
//                telemetry.addData("heading", toDegree(heading));
//                telemetry.addData("XYH", "%6f cm %6f cm", Posx, Posy);
                telemetry.update();
                if(gamepad1.start){
                    imu.resetYaw();
                    setpoint = 0;
                }
            }
        }
    }
}

