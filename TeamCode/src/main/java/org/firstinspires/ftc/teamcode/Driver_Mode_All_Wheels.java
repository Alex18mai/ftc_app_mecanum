package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import static java.lang.Math.abs;

@TeleOp (name = "Driver_Mode_All_Wheels", group = "Driver")

public class Driver_Mode_All_Wheels extends LinearOpMode {

    //motoare roti
    protected DcMotor Motor_FL = null;
    protected DcMotor Motor_FR = null;
    protected DcMotor Motor_BL = null;
    protected DcMotor Motor_BR = null;

    //motoare mecanisme

    //constante
    protected final int tics_per_cm = 67;
    protected final double deadzone = 0.1;

    @Override
    public void runOpMode()
    {
        initialise();

        waitForStart();

        while(opModeIsActive())
        {
            Motor_FL.setPower(0.9);
            Motor_FR.setPower(0.9);
            Motor_BL.setPower(0.9);
            Motor_BR.setPower(0.9);

        }
    }

    protected void initialise()
    {
        //hardware mapping
        Motor_FL = hardwareMap.dcMotor.get("Motor_FL");
        Motor_FR = hardwareMap.dcMotor.get("Motor_FR");
        Motor_BL = hardwareMap.dcMotor.get("Motor_BL");
        Motor_BR = hardwareMap.dcMotor.get("Motor_BR");

        //setare directii
        Motor_FL.setDirection(DcMotorSimple.Direction.REVERSE);
        Motor_FR.setDirection(DcMotorSimple.Direction.FORWARD);
        Motor_BL.setDirection(DcMotorSimple.Direction.REVERSE);
        Motor_BR.setDirection(DcMotorSimple.Direction.FORWARD);

        //setare
        Motor_FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor_FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor_BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor_BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //initializare putere
        Motor_FL.setPower(0);
        Motor_FR.setPower(0);
        Motor_BL.setPower(0);
        Motor_BR.setPower(0);

    }

    protected void gamepad_1(){
        if ( abs(gamepad1.left_stick_x) > deadzone || abs(gamepad1.left_stick_y) > deadzone || abs(gamepad1.right_stick_x) > deadzone)
            calculateWheelsPower(gamepad1.left_stick_y , gamepad1.left_stick_x , gamepad1.right_stick_x);
        else
            stop_walk();
    }

    protected void gamepad_2(){

    }

    protected void stop_walk(){
        Motor_FL.setPower(0);
        Motor_FR.setPower(0);
        Motor_BL.setPower(0);
        Motor_BR.setPower(0);
    }

    protected void calculateWheelsPower ( double drive, double strafe, double rotate )
    {
        double FL = Range.clip(drive + strafe + rotate , -0.7 , 0.7);
        double FR = Range.clip(drive - strafe - rotate , -0.7 , 0.7);
        double BL = Range.clip(drive - strafe + rotate , -0.7 , 0.7);
        double BR = Range.clip(drive + strafe - rotate , -0.7 , 0.7);

        /*telemetry.addData("FL : " , FL);
        telemetry.addData("FR : " , FR);
        telemetry.addData("BL : " , BL);
        telemetry.addData("BR : " , BR);
        telemetry.update();*/

        Motor_FL.setPower(FL);
        Motor_FR.setPower(FR);
        Motor_BL.setPower(BL);
        Motor_BR.setPower(BR);
    }

}