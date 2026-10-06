package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;


@TeleOp
public class flywheelTuner extends OpMode {

    public DcMotorEx shoota;
    public DcMotor intake;
    public DcMotor transfer;
    public Servo gate;
    private ElapsedTime timer = new ElapsedTime();
    private ElapsedTime velocityTimer = new ElapsedTime();
    double targetPower;
    double onSpeed = 1550;
    double intakePower;
    double transferPower;
    double gatePosition;
    boolean shootaOn;
    int lastPos = 0;
    double curVel = 0;

    private TelemetryManager telemetryM;


    double F = 11.6;
    double P = 0;
    double[] stepsizes = {10.0, 1.0, 0.1, 0.001, 0.0001, 0.00001};
    int stepindex = 1;

    @Override
    public void init() {
        intake = hardwareMap.get(DcMotor.class, "intakeMotor");
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        transfer = hardwareMap.get(DcMotor.class, "transferMotor");
        transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        transfer.setDirection(DcMotor.Direction.REVERSE);

        shoota = hardwareMap.get(DcMotorEx.class, "shoota");
        shoota.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shoota.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        shoota.setDirection(DcMotor.Direction.FORWARD);
        shoota.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        gate = hardwareMap.get(Servo.class, "gateServo");

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
    }
    public void start() {
        timer.reset();
        velocityTimer.reset();
    }

    @Override
    public void loop() {


        if (gamepad1.dpadDownWasPressed()) {
            if (shootaOn) {
                shootaOn = false;
            }
            else {
                shootaOn = true;
            }
        }
        int curPos = shoota.getCurrentPosition();
        double deltaTime = velocityTimer.seconds();

        if (deltaTime > 0.005) { // Protect against division by zero (e.g., 5ms minimum interval)
            int deltaTicks = curPos - lastPos;
            curVel = deltaTicks / deltaTime;

            // Reset for next loop iteration
            lastPos = curPos;
            velocityTimer.reset();
        }

        curVel = shoota.getVelocity();
        double error = targetPower - curVel;
        double V = shoota.getCurrent(CurrentUnit.AMPS);
        //-------------------------------------------------------------------------------------------------------

        if (gamepad1.right_bumper) {
            intakePower = 0.7;
            transferPower = 0.7;
        }
        else if (gamepad1.left_bumper) {
            intakePower = -0.7;
            transferPower = -0.7;
        }
        else {
            intakePower = 0;
            transferPower = 0;
        }

        if (gamepad1.right_trigger > .2) {
            gatePosition =.25;
            intakePower = 0.5;
            transferPower = 0.4;
        }
        else{
            gatePosition =.62;
        }

        intake.setPower(intakePower);
        transfer.setPower(transferPower);
        gate.setPosition(gatePosition);

        //-------------------------------------------------------------------------------------------------------

        if (gamepad2.bWasPressed()) {
            stepindex = (stepindex + 1) % stepsizes.length;
        }

        if (gamepad2.dpadLeftWasPressed()) {
            F -= stepsizes[stepindex];
        }

        if (gamepad2.dpadRightWasPressed()) {
            F += stepsizes[stepindex];
        }

        if (gamepad2.dpadUpWasPressed()) {
            P += stepsizes[stepindex];
        }

        if (gamepad2.dpadDownWasPressed()) {
            P -= stepsizes[stepindex];
        }

        if (gamepad2.yWasPressed()) {
            onSpeed += 10;
            targetPower += 10;
        }

        if (gamepad2.aWasPressed()) {
            onSpeed -= 10;
            targetPower -= 10;
        }


        double feedForwardTerm = targetPower * F;
        double feedbackTerm = error * P;
        double motorPower = feedForwardTerm + feedbackTerm;

        if (motorPower > 1.0) motorPower = 1.0;
        else if (motorPower < -1.0) motorPower = -1.0;

        if (shootaOn) {
            shoota.setPower(targetPower);
        }
        else {
            shoota.setPower(0);
        }


        telemetry.addData("target power", targetPower);
        telemetry.addData("current power", "%.6f", curVel);
        telemetry.addData("current power(RPMs)", "%.6f", (curVel / 28.0) * 60.0);
        telemetry.addData("current voltage", V);
        telemetry.addData("Error", "%.6f", targetPower - curVel);
        telemetry.addLine("-----------------------------------");
        telemetry.addData("P tune", "%.6f (D-Pad U/D)", P);
        telemetry.addData("F tune", "%.5f (D-Pad L/R)(round 0.0041 i think)", F);
        telemetry.addData("step size", "%.5f (B Button)", stepsizes[stepindex]);
        telemetry.addLine("-----------------------------------");
        telemetry.addData("gate position", gate.getPosition());
        telemetry.addData("intake speed", intakePower);
        telemetry.addData("transfer speed", transferPower);
        telemetry.addLine("-----------------------------------");
        telemetry.update();

        telemetryM.addData("target", (targetPower / 28.0) * 60.0);
        telemetryM.addData("cur vel", (curVel / 28.0) * 60.0);
        telemetryM.addData("voltage", V);
        telemetryM.addData("rpm max line", targetPower + 200);
        telemetryM.addData("rpm min line", -100);
        telemetryM.addData("voltage max line", 14);
        telemetryM.addData("voltage max line", -2);
        telemetryM.update(telemetry);
    }
}


/*


    private DcMotorEx flywheelMotor;
    private ElapsedTime timer = new ElapsedTime();

    public static double kF = 0.00035;
    public static double kP = 0.00080;

    public static double TARGET_VELOCITY = 2200.0;

    @Override
    public void runOpMode() throws InterruptedException {
        flywheelMotor = hardwareMap.get(DcMotorEx.class, "flywheel");

        // Reset and clear encoder states
        flywheelMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        // Custom math uses raw setPower, but requires encoder readings
        flywheelMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        flywheelMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        waitForStart();
        timer.reset();

        while (opModeIsActive()) {
            // Get current velocity (ticks/second)
            double currentVelocity = flywheelMotor.getVelocity();
            double error = TARGET_VELOCITY - currentVelocity;

            // Combined Feedforward-Feedback Control
            double feedForwardTerm = TARGET_VELOCITY * kF;
            double feedbackTerm = error * kP;
            double motorPower = feedForwardTerm + feedbackTerm;

            // Constrain motor power between -1.0 and 1.0
            if (motorPower > 1.0) motorPower = 1.0;
            else if (motorPower < -1.0) motorPower = -1.0;

            // Only power the motor if spinning forward is intended
            if (gamepad1.right_trigger > 0.1) {
                flywheelMotor.setPower(motorPower);
            } else {
                flywheelMotor.setPower(0);
            }



private DcMotorEx shooterMotor;
private ElapsedTime velocityTimer = new ElapsedTime();
private int lastPosition = 0;
private double currentVelocity = 0; // Ticks per second


int currentPosition = shooterMotor.getCurrentPosition();
double deltaTime = velocityTimer.seconds();

if (deltaTime > 0.005) { // Protect against division by zero (e.g., 5ms minimum interval)
    int deltaTicks = currentPosition - lastPosition;
    currentVelocity = deltaTicks / deltaTime;

    // Reset for next loop iteration
    lastPosition = currentPosition;
    velocityTimer.reset();
}

 */