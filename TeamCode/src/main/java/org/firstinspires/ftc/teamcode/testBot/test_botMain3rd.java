package org.firstinspires.ftc.teamcode.testBot;


import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.localization.localizers.PinpointLocalizer;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.panelsDrawing;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;




@TeleOp
public class test_botMain3rd extends OpMode {
    public DcMotor b_l_drive;
    public DcMotor b_r_drive;
    public DcMotor f_l_drive;
    public DcMotor f_r_drive;
    public DcMotorEx intake;
    double vertical;
    double horizontal;
    double pivot;

    double b_l_drivepower;
    double b_r_drivepower;
    double f_l_drivepower;
    double f_r_drivepower;
    public PinpointLocalizer local;
    double intakePower;
    double targetAngle = 0;
    double KP = 0.032;
    double error = 0;
    double lastError = 0;
    double angleTolerance = .5;
    double KD = 0.0018; //0.0020
    double curTime = 0;
    double lastTime = 0;
    double curHeading;

    double[] stepsizes = {1.0, 0.1, 0.001, 0.0001};
    int stepIndex = 2;

    private TelemetryManager telemetryM;

    panelsDrawing fieldDraw = new panelsDrawing();

    private Follower follower;


    @Override
    public void init() {

        b_l_drive = hardwareMap.get(DcMotor.class, "bldrive");
        b_l_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        b_l_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        b_r_drive = hardwareMap.get(DcMotor.class, "brdrive");
        b_r_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        b_r_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        b_r_drive.setDirection(DcMotor.Direction.REVERSE);

        f_l_drive = hardwareMap.get(DcMotor.class, "fldrive");
        f_l_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        f_l_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        f_r_drive = hardwareMap.get(DcMotor.class, "frdrive");
        f_r_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        f_r_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        f_r_drive.setDirection(DcMotor.Direction.REVERSE);

        intake = hardwareMap.get(DcMotorEx.class, "intakemotor");
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

        local = new PinpointLocalizer(hardwareMap, Constants.localizerConstants, new Pose( 72, 72, Math.toRadians(90)));

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        fieldDraw.init();

        follower = Constants.createFollower(hardwareMap);

        follower.setPose(new Pose(72,72,Math.toRadians(90)));
    }

    @Override
    public void loop() {

        local.update();
        follower.update();
        fieldDraw.updateFieldDraw(follower);

        vertical = -gamepad1.left_stick_y;
        horizontal = gamepad1.left_stick_x;
        pivot = gamepad1.right_stick_x;

        if (gamepad1.a) {
            follower.setHeading(Math.toRadians(90));
        }

        intakePower = 0;
        if (gamepad1.right_bumper) {
            intakePower = 1;
        } else if (gamepad1.left_bumper) {
            intakePower = -.5;
        }


        double theta = Math.atan2(vertical, horizontal);
        double r = Math.hypot(horizontal, vertical);
        telemetryM.addData("ver", vertical);
        telemetryM.addData("hori", horizontal);
        telemetryM.addData("theta1", theta);
        telemetryM.addData("r/hipo", r);

        // Second, rotate angle by the angle the robot is pointing
        theta = AngleUnit.normalizeRadians(theta -
                follower.getPose().getHeading());

        telemetryM.addData("heading", follower.getPose().getHeading());
        telemetryM.addData("heading", follower.getPose().getHeading());
        telemetryM.addData("theta2", theta);

        // Third, convert back to cartesian
        vertical = r * Math.sin(theta);
        horizontal = r * Math.cos(theta);
        telemetryM.addData("newVer", vertical);
        telemetryM.addData("newHori", horizontal);


        f_l_drivepower = vertical + pivot  + horizontal;
        f_r_drivepower = vertical - pivot  - horizontal;
        b_r_drivepower = vertical - pivot  + horizontal;
        b_l_drivepower = vertical + pivot  - horizontal;


        double maxdrivepower = 1.0;
        double maxdrivespeed = 1.0;

        maxdrivepower = Math.max(maxdrivepower, Math.abs(b_l_drivepower));
        maxdrivepower = Math.max(maxdrivepower, Math.abs(b_r_drivepower));
        maxdrivepower = Math.max(maxdrivepower, Math.abs(f_l_drivepower));
        maxdrivepower = Math.max(maxdrivepower, Math.abs(f_r_drivepower));

        b_l_drive.setPower(maxdrivespeed * (b_l_drivepower / maxdrivepower));
        b_r_drive.setPower(maxdrivespeed * (b_r_drivepower / maxdrivepower));
        f_l_drive.setPower(maxdrivespeed * (f_l_drivepower / maxdrivepower));
        f_r_drive.setPower(maxdrivespeed * (f_r_drivepower / maxdrivepower));


        intake.setPower(intakePower);


        telemetryM.update(telemetry);

        telemetryM.addData("x", follower.getPose().getX());
        telemetryM.addData("y", follower.getPose().getY());
        telemetryM.addData("Pose", follower.getPose());


    }
}
