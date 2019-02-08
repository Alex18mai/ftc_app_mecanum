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
    protected DcMotor Motor_Glisiera_L = null;
    protected DcMotor Motor_Glisiera_R = null;

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
        Motor_Glisiera_L = hardwareMap.dcMotor.get("Motor_Glisiera_L");
        Motor_Glisiera_R = hardwareMap.dcMotor.get("Motor_Glisiera_R");

        //initializare putere
        Motor_FL.setPower(0);
        Motor_FR.setPower(0);
        Motor_BL.setPower(0);
        Motor_BR.setPower(0);
        Motor_Glisiera_L.setPower(0);
        Motor_Glisiera_R.setPower(0);

        //setare directii
        Motor_FL.setDirection(DcMotorSimple.Direction.REVERSE);
        Motor_FR.setDirection(DcMotorSimple.Direction.FORWARD);
        Motor_BL.setDirection(DcMotorSimple.Direction.REVERSE);
        Motor_BR.setDirection(DcMotorSimple.Direction.FORWARD);
        Motor_Glisiera_L.setDirection(DcMotorSimple.Direction.REVERSE);
        Motor_Glisiera_R.setDirection(DcMotorSimple.Direction.FORWARD);

        //setare
        Motor_FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor_FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor_BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor_BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor_Glisiera_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor_Glisiera_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    protected void gamepad_1(){
        if ( abs(gamepad1.left_stick_x) > deadzone || abs(gamepad1.left_stick_y) > deadzone || abs(gamepad1.right_stick_x) > deadzone)
            calculateWheelsPower(gamepad1.left_stick_y , gamepad1.left_stick_x , gamepad1.right_stick_x);
        else
            stop_walk();
    }

    protected void gamepad_2(){
        if (gamepad2.a){
            Rotire_Glisiera_Encoder(0 , 0); //TODO : sa o duca la 90 de grade , viteza mare , pasi caluclati , ai grija la forward si reverse sa nu futa
        }
        else if (gamepad2.b){
            Rotire_Glisiera_Encoder(0 , 0); //TODO : sa o duca la 0 grade (de unde a pornit) , viteza mica , aceeasi pasi calculati dar cu minus
        }

    }

    protected void Rotire_Glisiera_Encoder(int pasi , double speed) {
        Motor_Glisiera_L.setTargetPosition(Motor_Glisiera_L.getCurrentPosition() + pasi * tics_per_cm);
        Motor_Glisiera_R.setTargetPosition(Motor_Glisiera_R.getCurrentPosition() + pasi * tics_per_cm);

        Motor_Glisiera_L.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Motor_Glisiera_R.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Motor_Glisiera_L.setPower(speed);
        Motor_Glisiera_R.setPower(speed);

        while (Motor_Glisiera_L.isBusy() || Motor_Glisiera_R.isBusy() && opModeIsActive()) {
            idle();
        }

        Motor_Glisiera_L.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Motor_Glisiera_R.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Motor_Glisiera_L.setPower(0);
        Motor_Glisiera_R.setPower(0);
    }

    protected void stop_walk(){
        Motor_FL.setPower(0);
        Motor_FR.setPower(0);
        Motor_BL.setPower(0);
        Motor_BR.setPower(0);
    }

    protected void calculateWheelsPower ( double drive, double strafe, double rotate )
    {
        double FL = Range.clip(drive - strafe + rotate , -0.7 , 0.7);
        double FR = Range.clip(drive + strafe - rotate , -0.7 , 0.7);
        double BL = Range.clip(drive + strafe + rotate , -0.7 , 0.7);
        double BR = Range.clip(drive - strafe - rotate , -0.7 , 0.7);

        Motor_FL.setPower(FL);
        Motor_FR.setPower(FR);
        Motor_BL.setPower(BL);
        Motor_BR.setPower(BR);
    }

}