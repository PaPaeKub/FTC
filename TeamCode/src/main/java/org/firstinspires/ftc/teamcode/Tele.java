package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.Utilize.WrapRads;
import static org.firstinspires.ftc.teamcode.Utilize.toRadian;
import static org.firstinspires.ftc.teamcode.Utilize.toDegree;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name="Tele")
public  class Tele extends Robot {

    private Controller controller;

    // Variables
    int targetLift = 0;
    double setpoint = 0, H_Ang = 0, AL_Ang = 0, AD_Ang = 0;
    boolean autoLift = false, V_Pressed = false, VisBusy = false, ITisOn = false, tp_Pressed = false,
            H_disable = false, r_disable = false, hl_Pressed = false, ll_Pressed = false;
    double CurrentTime = System.nanoTime() * 1E-9,  lastRXtime = CurrentTime;

    private void Init() {
        // Initialize Robot
        Initialize(DcMotor.RunMode.RUN_WITHOUT_ENCODER, new double[]{0, 0, AL_Ang},
                new double[]{0, 0, 0, 0});

        controller = new Controller(1.0, 0.05, 0.1, 0 , 0.2, toRadian(0.75));


        setpoint = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    private void Movement() {
        CurrentTime = System.nanoTime() * 1E-9;
        double speed = 0.275;
        double lx = -gamepad1.left_stick_x;
        double ly = -gamepad1.left_stick_y;
        double x1 = gamepad1.dpad_left ? speed : gamepad1.dpad_right ? speed : lx;
        double y1 = gamepad1.dpad_up ? speed : gamepad1.dpad_down ? -speed : ly;
        double yaw = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double x2 = (Math.cos(yaw) * x1) - (Math.sin(yaw) * y1);
        double y2 = (Math.sin(yaw) * x1) + (Math.cos(yaw) * y1);
        // Rotate
        double r = r_disable ? 0 : controller.Calculate(WrapRads(setpoint - yaw));
        double x = -gamepad1.right_stick_x;
        if (x != 0 || CurrentTime - lastRXtime < 0.45) {
            r = x;
            setpoint = yaw;
        }
        if (lx == 0 && ly == 0 && x== 0 && Math.abs(r) < 0.2)  r = 0;
        lastRXtime = x != 0 ? CurrentTime : lastRXtime;
        // Denominator for division to get no more than 1
        double d = Math.max(Math.abs(x2) + Math.abs(y2) + Math.abs(r), 0.5);
        MovePower((y2 - x2 - r) / d, (y2 + x2 + r) / d,
                (y2 + x2 - r) / d, (y2 - x2 + r) / d);
        telemetry.addData("yaw", toDegree(yaw));
        telemetry.addData("setpoint", toDegree(setpoint));
        telemetry.addData("error", controller.Error);
    }

    public void lift(){
        double  curPos     = Math.max(LL.getCurrentPosition(), RL.getCurrentPosition());
        double LT = gamepad1.left_trigger;
        double RT = gamepad1.right_trigger;
        double Lpos = LL.getCurrentPosition();
        double Rpos = RL.getCurrentPosition();
        boolean lt_Pressed = LT >= 0.25;
        boolean rl_Pressed = RT >= 0.25;
        double Lift_Power = lt_Pressed ? (curPos < 0          ?  0   : -LT) :
                rl_Pressed ? (curPos > 4800       ?  0   :  RT) :
                        autoLift   ? (curPos > targetLift ? -0.3 :  1)  : 0;
        LiftPower(Lift_Power);

    }

    @Override
    public void runOpMode() {
        Init();
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                Odomentry();
                Movement();
                lift();

//                telemetry.addData("XYH", "%6f cm %6f cm", Posx, Posy);
                telemetry.addData("LRM", "%6d  %6d %6d", left_encoder_pos, right_encoder_pos, center_encoder_pos);
                telemetry.addData("heading", toDegree(heading));
                telemetry.addData("XYH", "%6f cm %6f cm", Posx, Posy);
                telemetry.update();
                if(gamepad1.a){
                    LA.getController().pwmDisable();
                }

            }
        }
    }
}