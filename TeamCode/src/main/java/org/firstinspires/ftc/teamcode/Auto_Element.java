package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.Set;

@Autonomous(name="Auto_Element")
public class Auto_Element extends Robot {
    int i = 0;

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

    private void clips() {
        SetServoPos(0.66, LLG, RLG);
        sleep(1000);

        SetServoPos(0.2, D);
        sleep(100);
        SetServoPos(0, LLG, RLG);
    }

    public void runOpMode() {
        Init();

        while (!(LTS.isPressed()) && !(RTS.isPressed())) {
            double spl = LTS.isPressed() ? 0 : -0.4;
            double spr = RTS.isPressed() ? 0 : -0.4;
            Lift_SetPower(spl, spr);
        }
        LiftPower(0);
        SetServoPos(0.5, LAG, RAG);

        LL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        LL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        WaitForStart();
        if (opModeIsActive()) {
            move(0, -0.2, 1.1, 0.0, new double[]{0.5, 0.04, 0.3},
                    new double[]{2.0, 0.0001, 0.3, 0.0}, new double[]{0.12, 0.001, 0.007, 0.0}, new double[]{0.25, 0.001, 0.03, 0.0}, 0.0001, High_Chamber);

            clips();

            move(0, 1.0, 1.2, 0.0, new double[]{0.5, 0.1, 0.3},
                    new double[]{2.0, 0.0001, 0.3, 0.0}, new double[]{0.12, 0.001, 0.007, 0.0}, new double[]{0.25, 0.001, 0.03, 0.0}, 0.0001, 0);

            move(0, 1.0, 2.1, 0.0, new double[]{0.5, 0.1, 0.3},
                    new double[]{2.0, 0.0001, 0.3, 0.0}, new double[]{0.12, 0.001, 0.007, 0.0}, new double[]{0.25, 0.001, 0.03, 0.0}, 0.0001, 0);

            move(0, 1.4, 0.5, 0.0, new double[]{0.5, 0.1, 0.3},
                    new double[]{2.0, 0.0001, 0.3, 0.0}, new double[]{2.0, 0.001, 0.09, 0.0}, new double[]{0.25, 0.001, 0.03, 0.0}, 0.0001, 0);

            move(0, 1.4, 2.1, 0.0, new double[]{0.5, 0.1, 0.3},
                    new double[]{2.0, 0.0001, 0.3, 0.0}, new double[]{0.12, 0.001, 0.09, 0.0}, new double[]{0.25, 0.001, 0.03, 0.0}, 0.0001, 0);

            move(0, 1.8, 0.5, 0.0, new double[]{0.5, 0.1, 0.3},
                    new double[]{2.0, 0.0001, 0.3, 0.0}, new double[]{2.0, 0.001, 0.09, 0.0},  new double[]{0.25, 0.001, 0.03, 0.0}, 0.0001, 0);

            move(0, 1.8, 2.1, 0.0, new double[]{0.5, 0.1, 0.3},
                    new double[]{2.0, 0.0001, 0.3, 0.0}, new double[]{0.12, 0.001, 0.09, 0.0}, new double[]{0.25, 0.001, 0.03, 0.0}, 0.0001, 0);

            move(0, 2.05, 0.5, 0.0, new double[]{0.5, 0.1, 0.3},
                    new double[]{2.0, 0.0001, 0.3, 0.0}, new double[]{2.0, 0.001, 0.09, 0.0}, new double[]{0.25, 0.001, 0.03, 0.0}, 0.0001, 0);

            SetServoPos(0.75, LLG, RLG);
            SetServoPos(0.2, D);

            move(0.4, 1.0, 1.0, 180.0, new double[]{0.5, 0.1, 0.3},
                    new double[]{2.0, 0.001, 0.07, 0.0}, new double[]{2.0, 0.001, 0.09, 0.0}, new double[]{0.25, 0.0005, 0.009, 0.0}, 0.1, 0);

            move(0.4, 1.0, 0.35, 180.0, new double[]{0.5, 0.1, 0.3},
                    new double[]{2.0, 0.01, 0.08, 0.0}, new double[]{2.0, 0.001, 0.09, 0.0}, new double[]{0.25, 0.0001, 0.009, 0.0}, 0.1, 0);

            SetServoPos(0, D);
            SetServoPos(0, LLG, RLG);

            move(0.4, -0.4, 1.15, 0.0, new double[]{0.5, 0.1, 0.3},
                   new double[]{4.0, 0.01, 0.8, 0.0}, new double[]{2.0, 0.001, 0.09, 0.0}, new double[]{0.25, 0.0001, 0.009, 0.0}, 0.1, High_Chamber);

            clips();

//            move(0.4, 2.15, 0.4, 180.0, new double[]{0.4, 0.001, 0.05},
//                    new double[]{4.0, 0.01, 0.8, 0.0}, new double[]{0.12, 0.01, 0.4, 0.0}, new double[]{0.15, 0.008, 0.0001, 0.0}, 0.1, 0);

        }
    }
}