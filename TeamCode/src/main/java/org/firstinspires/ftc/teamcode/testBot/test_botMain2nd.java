package org.firstinspires.ftc.teamcode.testBot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@TeleOp
public class test_botMain2nd extends OpMode {

    public DcMotor b_l_drive;
    public DcMotor b_r_drive;
    public DcMotor f_l_drive;
    public DcMotor f_r_drive;
    double vertical;
    double horizontal;
    double pivot;

    double b_l_drivepower;
    double b_r_drivepower;
    double f_l_drivepower;
    double f_r_drivepower;
    double targetAngle = 0;
    double KP = 0.032;
    double error = 0;
    double lastError = 0;
    double angleTolerance = .5;
    double KD = 0.0018; //0.0020
    double curTime = 0;
    double lastTime = 0;
    double curHeading;
    double maxdrivespeed = 1.0;

    double[] stepsizes = {1.0, 0.1, 0.001, 0.0001};
    int stepIndex = 2;


    @Override
    public void init() {
        b_l_drive = hardwareMap.get(DcMotor.class, "b-l-drive");
        b_l_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        b_l_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        b_r_drive = hardwareMap.get(DcMotor.class, "b-r-drive");
        b_r_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        b_r_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        b_r_drive.setDirection(DcMotor.Direction.REVERSE);

        f_l_drive = hardwareMap.get(DcMotor.class, "f-l-drive");
        f_l_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        f_l_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        f_r_drive = hardwareMap.get(DcMotor.class, "f-r-drive");
        f_r_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        f_r_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        f_r_drive.setDirection(DcMotor.Direction.REVERSE);



    }

    @Override
    public void loop() {

        vertical = gamepad1.left_stick_y;
        horizontal = gamepad1.left_stick_x;
        pivot = gamepad1.right_stick_x;




        b_l_drivepower = vertical - pivot  + horizontal;
        b_r_drivepower = vertical + pivot  - horizontal;
        f_l_drivepower = vertical - pivot  - horizontal;
        f_r_drivepower = vertical + pivot  + horizontal;

        if (gamepad1.aWasPressed() && (maxdrivespeed < 1.0)) {
            maxdrivespeed = 1.0;
        }
        else if (gamepad1.aWasPressed() && (maxdrivespeed == 1.0)) {
            maxdrivespeed = 0.5;
        }

        b_l_drive.setPower(maxdrivespeed * b_l_drivepower );
        b_r_drive.setPower(maxdrivespeed * b_r_drivepower );
        f_l_drive.setPower(maxdrivespeed * f_l_drivepower );
        f_r_drive.setPower(maxdrivespeed * f_r_drivepower );

    }
}
