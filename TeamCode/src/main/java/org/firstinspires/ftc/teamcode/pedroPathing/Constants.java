package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PredictiveBrakingCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {


    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(7)
            //7.983226 old mass
            .forwardZeroPowerAcceleration(-32.27066)
            .lateralZeroPowerAcceleration(-43.93632)
            .translationalPIDFCoefficients(new PIDFCoefficients(.1,0,.008, .0115))
            .headingPIDFCoefficients(new PIDFCoefficients(.6, 0, .05, .02))
            //.drivePIDFCoefficients(new FilteredPIDFCoefficients(.6, 0, .0002, .6, .02))
            //.predictiveBrakingCoefficients(new PredictiveBrakingCoefficients(kP, kLinear, kQuadratic))
            /*

            .centripetalScaling(0)*/
            ;
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("f-r-drive")
            .rightRearMotorName("b-r-drive")
            .leftRearMotorName("b-l-drive")
            .leftFrontMotorName("f-l-drive")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .xVelocity(79.50454880121185)
            .yVelocity(48.09418205201156)

            ;

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(2.99213) //76
            .strafePodX(-5.66929) //-144
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    public static PathConstraints pathConstraints = new PathConstraints(0.99,
            100,
            1,
            1);


    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();


    }
}
