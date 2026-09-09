package org.firstinspires.ftc.teamcode;


import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


@TeleOp
public class testauto extends OpMode {
    private Follower follower;
    private Timer pathTimer, opModeTimer;

    private TelemetryManager telemetryM;






    public enum PathState {
        //states Ex: Start, Drive, Shoot(or whatever act is needed for the cur game), End.

        DRIVE,
        END
    }

    PathState pathState;

    private final Pose startPose = new Pose(72, 24, Math.toRadians(135));

    private final Pose checkPoint = new Pose(24, 72, Math.toRadians(135));
    private final Pose endPose = new Pose(75, 120, Math.toRadians(270));

    private PathChain driveOne;

    public DcMotorEx intake;



    public void buildPaths() {
        driveOne = follower.pathBuilder()
                .addPath(new BezierLine(startPose, checkPoint))
                .setTangentHeadingInterpolation()
                .addPath(new BezierLine(checkPoint, endPose))
                .setLinearHeadingInterpolation(checkPoint.getHeading(), endPose.getHeading())
                .build();
    }

    public void statePathUpdate() {
        switch(pathState) {
            case DRIVE:
                follower.followPath(driveOne, true);
                setPathState(PathState.END);
                break;
            case END:
                telemetryM.addLine("auto is over");
            default:
                telemetryM.addLine("no cur state");
                break;
        }
    }

    public void setPathState(PathState newState) {
        pathState = newState;
        pathTimer.resetTimer();
    }





    @Override
    public void init() {
        pathState = PathState.DRIVE;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        buildPaths();

        follower.setPose(startPose);
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        intake = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);

    }

    public void start() {
        opModeTimer.resetTimer();
        setPathState(pathState);
    }
    @Override
    public void loop() {
        follower.update();
        statePathUpdate();
        telemetryM.update(telemetry);

        telemetryM.addData("path state", pathState.toString());
        telemetryM.addData("cur X", follower.getPose().getX());
        telemetryM.addData("cur Y", follower.getPose().getY());
        telemetryM.addData("cur heading", follower.getPose().getHeading());
        telemetryM.addData("path timer", pathTimer.getElapsedTimeSeconds());

    }
}
