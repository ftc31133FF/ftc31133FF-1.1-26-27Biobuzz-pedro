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
            .mass(7.983226)
            .forwardZeroPowerAcceleration(-31.113625787979135)
            .lateralZeroPowerAcceleration(-55.63333943825847)
            .translationalPIDFCoefficients(new PIDFCoefficients(.12,0,.015, .0115))
            .headingPIDFCoefficients(new PIDFCoefficients(.65, 0, .08, .02))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(.6, 0, .0002, .6, .02))
            //.predictiveBrakingCoefficients(new PredictiveBrakingCoefficients(kP, kLinear, kQuadratic))
            /*

            .centripetalScaling(0)*/
            ;
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("frdrive")
            .rightRearMotorName("brdrive")
            .leftRearMotorName("bldrive")
            .leftFrontMotorName("fldrive")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .xVelocity(90.79039854515256)
            .yVelocity(66.35672117218259)

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
