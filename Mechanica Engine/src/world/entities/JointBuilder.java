package world.entities;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.WheelJoint;
import org.jbox2d.dynamics.joints.WheelJointDef;
import statics.G;

/**
 * Created by domin on 11 Aug 2017.
 */
public class JointBuilder {
    //ConstantVolumeJoint, DistanceJoint, FrictionJoint, GearJoint, MotorJoint, MouseJoint, PrismaticJoint, PulleyJoint, RevoluteJoint, RopeJoint, WeldJoint, WheelJoint
    private Body body1, body2;
    Vec2 position;
    Vec2  axis = new Vec2(0f, 1f);
    float motorSpeed = 10f;
    float maxMotorTorque = 50f;
    boolean enableMotor = false;
    float frequencyHz = 4f;
    float dampingRatio = 0.7f;


    public JointBuilder(Body body1, Body body2){
        this.body1 = body1;
        this.body2 = body2;
        position = body2.getPosition();
    }

    public JointBuilder setPosition(Vec2 position) {
        this.position = position;
        return this;
    }

    public JointBuilder setAxis(Vec2 axis) {
        this.axis = axis;
        return this;
    }

    public JointBuilder setMotorSpeed(float motorSpeed) {
        this.motorSpeed = motorSpeed;
        return this;
    }

    public JointBuilder setMaxMotorTorque(float maxMotorTorque) {
        this.maxMotorTorque = maxMotorTorque;
        return this;
    }

    public JointBuilder setEnableMotor(boolean enableMotor) {
        this.enableMotor = enableMotor;
        return this;
    }

    public JointBuilder setFrequencyHz(float frequencyHz) {
        this.frequencyHz = frequencyHz;
        return this;
    }

    public JointBuilder setDampingRatio(float dampingRatio) {
        this.dampingRatio = dampingRatio;
        return this;
    }

    public WheelJoint buildWheelJoint(){
        WheelJointDef jointDef = new WheelJointDef();
        jointDef.initialize(body1, body2, position, axis);
        jointDef.motorSpeed = motorSpeed;
        jointDef.maxMotorTorque = maxMotorTorque;
        jointDef.enableMotor = enableMotor;
        jointDef.frequencyHz = frequencyHz;
        jointDef.dampingRatio = dampingRatio;
        return (WheelJoint) G.world.createJoint(jointDef);
    }

}
