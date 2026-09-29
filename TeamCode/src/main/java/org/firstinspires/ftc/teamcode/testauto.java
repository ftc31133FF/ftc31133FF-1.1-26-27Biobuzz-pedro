package org.firstinspires.ftc.teamcode;


import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
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
/*
    private final Pose startPosed = new Pose(72, 24, Math.toRadians(135));
    private final Pose checkPoint = new Pose(24, 72, Math.toRadians(135));
    private final Pose endPose = new Pose(75, 120, Math.toRadians(270));

 */
    private final Pose startPose = new Pose(60, 9, Math.toRadians(0));

    private final double boxRadius = 54;
    public final double cornerRadius = 36;
    private final Pose bottomRight = new Pose(72 + boxRadius, 72 - boxRadius, Math.toRadians(0));
    private final Pose topRight = new Pose(72 + boxRadius, 72 + boxRadius, Math.toRadians(0));
    private final Pose topLeft = new Pose(72 - boxRadius, 72 + boxRadius, Math.toRadians(0));
    private final Pose bottomLeft = new Pose(72 - boxRadius, 72 - boxRadius, Math.toRadians(0));

    private PathChain driveOne, MainChain, initialLineUp, MainChainButFancy;

    public DcMotorEx intake;



    public void buildPaths() {
        /*
        driveOne = follower.pathBuilder()
                .addPath(new BezierLine(startPose, checkPoint))
                .setTangentHeadingInterpolation()
                .addPath(new BezierLine(checkPoint, endPose))
                .setLinearHeadingInterpolation(checkPoint.getHeading(), endPose.getHeading())
                .build();

         */
        initialLineUp = follower.pathBuilder()
                .addPath(new BezierLine(
                        startPose, new Pose(78, 18)
                ))
                .setConstantHeadingInterpolation(0)
                .build();

        MainChain = follower.pathBuilder()

                .addPath(new BezierLine(
                                new Pose(126, 18), new Pose(126, 126)
                        ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                                new Pose(126, 126), new Pose(18, 126)
                        ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                                new Pose(18, 126), new Pose(18, 18)
                        ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                                new Pose(18, 18), new Pose(126, 18)
                        ))
                .setTangentHeadingInterpolation()

                .build();
        MainChainButFancy = follower.pathBuilder()

                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()
                .addPath(new BezierCurve(
                        new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY()), bottomRight, new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomRight.getX(), bottomRight.getY() + cornerRadius), new Pose(topRight.getX(), topRight.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topRight.getX(), topRight.getY() - cornerRadius), topRight, new Pose(topRight.getX() - cornerRadius, topRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topRight.getX() - cornerRadius, topRight.getY()), new Pose(topLeft.getX() + cornerRadius, topLeft.getY())
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(topLeft.getX() + cornerRadius, topLeft.getY()), topLeft, new Pose(topLeft.getX(), topLeft.getY() - cornerRadius)
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(topLeft.getX(), topLeft.getY() - cornerRadius), new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius)
                ))
                .setTangentHeadingInterpolation()



                .addPath(new BezierCurve(
                        new Pose(bottomLeft.getX(), bottomLeft.getY() + cornerRadius), bottomLeft, new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY())
                ))
                .setTangentHeadingInterpolation()

                .addPath(new BezierLine(
                        new Pose(bottomLeft.getX() + cornerRadius, bottomLeft.getY()), new Pose(bottomRight.getX() - cornerRadius, bottomRight.getY())
                ))
                .setTangentHeadingInterpolation()

                .build();
    }

    public void statePathUpdate() {
        switch(pathState) {
            case DRIVE:
                follower.followPath(initialLineUp, true);
                follower.followPath(MainChainButFancy, true);

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
