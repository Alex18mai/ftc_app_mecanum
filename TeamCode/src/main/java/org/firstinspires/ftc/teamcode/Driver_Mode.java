package org.firstinspires.ftc.teamcode;

import android.support.annotation.IntRange;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Math.abs;

@TeleOp (name = "Driver_Mode", group = "Driver")

public class Driver_Mode extends LinearOpMode {

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
            gamepad_1();
            gamepad_2();
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
        Motor_FL.setDirection(DcMotorSimple.Direction.FORWARD);
        Motor_FR.setDirection(DcMotorSimple.Direction.FORWARD);
        Motor_BL.setDirection(DcMotorSimple.Direction.FORWARD);
        Motor_BR.setDirection(DcMotorSimple.Direction.FORWARD);

        //setare mod
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
            calculateWheelsPower(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
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

    protected void calculateWheelsPower ( double left_X, double left_Y, double right_X )
    {
        double r = Math.hypot(left_X, left_Y);
        double angle = Math.atan2(left_X, left_Y) - Math.PI / 4;

        double FL = Range.clip(r * Math.cos(angle) + right_X , -0.9 , 0.9);
        double FR = Range.clip(r * Math.sin(angle) - right_X , -0.9 , 0.9);
        double BL = Range.clip(r * Math.sin(angle) + right_X , -0.9 , 0.9);
        double BR = Range.clip(r * Math.cos(angle) - right_X , -0.9 , 0.9);

        telemetry.addData("FL : " , FL);
        telemetry.addData("FR : " , FR);
        telemetry.addData("BL : " , BL);
        telemetry.addData("BR : " , BR);
        telemetry.update();

        Motor_FL.setPower(FL);
        Motor_FR.setPower(FR);
        Motor_BL.setPower(BL);
        Motor_BR.setPower(BR);
    }

    //calculeaza puterile rotilor in functie de stick si apoi aplica rotilor puterea
    /*protected void calculateWheelsPower ( double left_X, double left_Y, double RotationChange )
    {
        power_Motor_FLBR = left_Y - left_X;
        power_Motor_FRBL = left_Y + left_X;
        power_Motor_LEFT = RotationChange;
        power_Motor_RIGHT = -RotationChange;
        setWheelsPower();
    }*/

    //aplica rotilor puterea
    /*protected void setWheelsPower()
    {
        Motor_FL.setPower(-power_Motor_LEFT);
        Motor_FR.setPower(power_Motor_RIGHT);
        Motor_BL.setPower(power_Motor_LEFT);
        Motor_BR.setPower(power_Motor_RIGHT);
    }*/
}