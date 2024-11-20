package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Auto_Bucket")
public class Auto_Bucket extends Robot {

    private void keep(double pos) {
        SetServoPos(0, LLG, RLG);
        SetServoPos(0.34, LA, RA);
        SetServoPos(0.15, G);
        SetServoPos(pos, AG);
        SetServoPos(0.3, D);
        SetServoPos(0.15, LAG, RAG);
        SetServoPos(0.71, LFA, RFA);

        sleep(500);

        SetServoPos(0, G);
        sleep(300);
        SetServoPos(0, AG);
        SetServoPos(1, LAG, RAG);
        SetServoPos(0, LFA, RFA);
        sleep(500);
        SetServoPos(0, LA, RA);
        sleep(300);
        SetServoPos(0, D);
        sleep(300);
        SetServoPos(0.2, G);
        sleep(100);
        SetServoPos(0, LAG, RAG);
    }

    private void drop() {
        SetServoPos(1, LLG, RLG); // 0.59

        sleep(500);

        SetServoPos(0.5, D);

        sleep(400);

        SetServoPos(0, LLG, RLG);
        SetServoPos(0, D);
    }

    private void Init() {
        // Initialize Robot
        Initialize(DcMotor.RunMode.RUN_WITHOUT_ENCODER, new double[]{0, 0, 0},
                new double[]{0.3, 0, 0.1, 0});
        imu.resetYaw();
    }

    private void WaitForStart() {
        while (!isStarted() && !isStopRequested()) {
            Posx = 0;
            Posy = 0;
            heading = 0;
        }
    }

    public void runOpMode() {
        Init();

        while (!(LTS.isPressed())) {
            double power = LTS.isPressed() ? 0 : -0.4;
            Lift_SetPower(power, power);
        }
        LiftPower(0);
        SetServoPos(0.5, LAG, RAG);

        LL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        LL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        WaitForStart();

        if (opModeIsActive()) {
            move(1.0, -0.9, 0.6, 218.0, new double[]{0.35, 0.35, 0.35},
                    new double[]{2.0, 0.01, 0.09, 0.0}, new double[]{0.2, 0.009, 0.01, 0.0}, new double[]{0.25, 0.007, 0.001, 0.0}, 0.0001, High_Basket);

            drop();

            move(1.0, -0.7, 0.67, 189.0, new double[]{0.35, 0.35, 0.35},
                    new double[]{2.0, 0.002, 0.15, 0.0}, new double[]{0.4, 0.009, 0.04, 0.0}, new double[]{0.25, 0.007, 0.01, 0.0}, 0.0001, 0);

            keep(0);

            move(1.0, -0.9, 0.6, 218.0, new double[]{0.35, 0.35, 0.35},
                    new double[]{2.0, 0.01, 0.09, 0.0}, new double[]{0.2, 0.009, 0.01, 0.0}, new double[]{0.25, 0.007, 0.001, 0.0}, 0.0001, High_Basket);

            drop();

            move(1.0, -0.96, 0.65, 180.0, new double[]{0.35, 0.35, 0.35},
                    new double[]{2.0, 0.002, 0.15, 0.0}, new double[]{0.4, 0.01, 0.025, 0.0}, new double[]{0.25, 0.007, 0.01, 0.0}, 0.0001, 0);

            keep(0);

            move(1.0, -0.9, 0.6, 218.0, new double[]{0.35, 0.35, 0.35},
                    new double[]{2.0, 0.01, 0.09, 0.0}, new double[]{0.2, 0.009, 0.01, 0.0}, new double[]{0.25, 0.007, 0.001, 0.0}, 0.0001, High_Basket);

            drop();

            move(1.0, -0.9, 0.73, 150.0, new double[]{0.35, 0.35, 0.35},
                    new double[]{2.0, 0.002, 0.05, 0.0}, new double[]{0.4, 0.007, 0.001, 0.0}, new double[]{0.25, 0.007, 0.01, 0.0}, 0.0001, 0);

            keep(0.15);

            move(1.0, -0.9, 0.6, 218.0, new double[]{0.35, 0.35, 0.35},
                    new double[]{2.0, 0.01, 0.09, 0.0}, new double[]{0.2, 0.007, 0.01, 0.0}, new double[]{0.25, 0.007, 0.001, 0.0}, 0.0001, High_Basket);

            drop();

            move(1.0, 0.45, 2.1, 90.0, new double[]{0.35, 0.35, 0.35},
                    new double[]{2.0, 0.01, 0.2, 0.0}, new double[]{0.09, 0.01, 0.01, 0.0}, new double[]{0.25, 0.007, 0.01, 0.0}, 0.0001, 800);

            SetServoPos(1, LLG, RLG); // 0.55

            sleep(10000);
        }
    }
}