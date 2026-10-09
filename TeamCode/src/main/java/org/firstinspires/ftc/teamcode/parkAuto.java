package org.firstinspires.ftc.teamcode;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class parkAuto extends OpMode {

    public DcMotor b_l_drive;
    public DcMotor b_r_drive;
    public DcMotor f_l_drive;
    public DcMotor f_r_drive;
    double b_l_drivePower;
    double b_r_drivePower;
    double f_l_drivePower;
    double f_r_drivePower;

    @Override
    public void init() {
        b_l_drive = hardwareMap.get(DcMotor.class, "b-l-drive");
        b_l_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        b_l_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        b_r_drive = hardwareMap.get(DcMotor.class, "b-r-drive");
        b_r_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        b_r_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        b_l_drive.setDirection(DcMotor.Direction.REVERSE);

        f_l_drive = hardwareMap.get(DcMotor.class, "f-l-drive");
        f_l_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        f_l_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        f_r_drive = hardwareMap.get(DcMotor.class, "f-r-drive");
        f_r_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        f_r_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        f_l_drive.setDirection(DcMotor.Direction.REVERSE);
    }
    public void loop() {
        b_l_drivePower = -.5;
        b_r_drivePower = .5;
        f_l_drivePower = .5;
        f_r_drivePower = -.5;
        sleep(3000);
        b_l_drivePower = .5;
        b_r_drivePower = -.5;
        f_l_drivePower = -.5;
        f_r_drivePower = .5;
        sleep(3010);
        b_l_drivePower = 0;
        b_r_drivePower = 0;
        f_l_drivePower = 0;
        f_r_drivePower = 0;


    }
}