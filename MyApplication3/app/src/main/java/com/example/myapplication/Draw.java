package com.example.myapplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


public class Draw extends View {

    public Draw(Context context, AttributeSet attr){
        super(context, attr);
        setOnTouchListener(new MyTouchListener());
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        initial();
    }

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    DrawObj torso;
    Point DragStart;
    Point coordinate;

    DrawObj selectObj = null;

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        torso.draw(canvas);
    }
    public void reset(){
        torso = null;
        DragStart = null;
        coordinate = null;
        selectObj = null;
        initial();
        this.invalidate();

    }
    private final class MyTouchListener implements OnTouchListener{
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mScaleDetector.onTouchEvent(motionEvent);
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                DragStart = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
                selectObj = torso.select(DragStart);
                if(selectObj != null)
                    coordinate = selectObj.coordinante;
                return true;
            }
            else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                if(selectObj!=null){
                    if(selectObj == torso){
                        int x = DragStart.x - coordinate.x;
                        int y = DragStart.y - coordinate.y;
                        torso.coordinante = new Point((int)(motionEvent.getX()-x),
                                (int)(motionEvent.getY()-y));
                        torso.actualCoor = torso.coordinante;
                        updateActudalCoordinate(torso);
                        view.invalidate();
                    }
                    else if(selectObj.type == DrawObj.Type.Head){
                        int clockwise = motionEvent.getX()>=selectObj.actualCoor.x ? 1 : -1;
                        Point current = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
                        float rotate = getRotateAngle(current,selectObj.actualCoor);
                        rotate = rotate > 50 ? 50:rotate;
                        selectObj.rotate = clockwise * rotate;

                        view.invalidate();
                    }
                    else if(selectObj.type == DrawObj.Type.ArmUpperRight||
                            selectObj.type == DrawObj.Type.LegUpperRight||
                            selectObj.type == DrawObj.Type.LegUpperLeft||
                            selectObj.type == DrawObj.Type.ArmUpperLeft){
                        int clockwise = motionEvent.getX()>=selectObj.actualCoor.x ? -1 : 1;
                        Point current = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
                        float rotate = (float) 180 - getRotateAngle(current,selectObj.actualCoor);

                        if(selectObj.type == DrawObj.Type.LegUpperRight||
                                selectObj.type == DrawObj.Type.LegUpperLeft){
                            rotate = rotate > 90 ? 90 :rotate;
                        }

                        selectObj.rotate = clockwise * rotate;
                        selectObj.actualRotate = clockwise * rotate;

                        view.invalidate();
                    }
                    else if(selectObj.type == DrawObj.Type.ArmLowerLeft||
                            selectObj.type == DrawObj.Type.ArmLowerRight||
                            selectObj.type == DrawObj.Type.LegLowerRight||
                            selectObj.type == DrawObj.Type.LegLowerLeft
                    ){

                        Point current = new Point((int)motionEvent.getX(), (int)motionEvent.getY());

                        Point parentCoor = selectObj.parent.actualCoor;
                        float parentRotate = selectObj.parent.actualRotate;
                        Point selectCoor = selectObj.coordinante;

                        double rad = Math.toRadians((double)parentRotate);

                        Point p = new Point((int)(selectCoor.x*Math.cos(rad) - selectCoor.y*Math.sin(rad)),
                                (int)(selectCoor.x*Math.sin(rad) + selectCoor.y*Math.cos(rad)));

                        selectObj.actualCoor = new Point(parentCoor.x + p.x, parentCoor.y+p.y);

                        int clockwise = motionEvent.getX()>=selectObj.actualCoor.x ? -1 : 1;
                        float actual_rotate = (float) 180 - getRotateAngle(current,selectObj.actualCoor);
                        float rotate = actual_rotate - clockwise*selectObj.parent.rotate;
                        rotate = clockwise * rotate;

                        if(selectObj.type == DrawObj.Type.ArmLowerLeft||
                                selectObj.type == DrawObj.Type.ArmLowerRight) {
                            float temp;
                            if(rotate> 135)
                                temp = 135;
                            else if(rotate < -135)
                                temp = -135;
                            else
                                temp = rotate;

                            rotate = temp;
                        }
                        else {
                            float temp;

                            if (rotate > 90)
                                temp = 90;
                            else if (rotate < -90)
                                temp = -90;
                            else
                                temp = rotate;
                            rotate = temp;
                        }
                        selectObj.rotate = rotate;
                        selectObj.actualRotate = actual_rotate;
                        view.invalidate();
                    }
                    else{
                        Point current = new Point((int)motionEvent.getX(), (int)motionEvent.getY());

                        Point parentCoor = selectObj.parent.parent.actualCoor;
                        float parentRotate = selectObj.parent.parent.rotate;
                        Point selectCoor = selectObj.parent.coordinante;

                        double rad = Math.toRadians((double)parentRotate);

                        Point p = new Point((int)(selectCoor.x*Math.cos(rad) - selectCoor.y*Math.sin(rad)),
                                (int)(selectCoor.x*Math.sin(rad) + selectCoor.y*Math.cos(rad)));

                        selectObj.parent.actualCoor = new Point(parentCoor.x + p.x, parentCoor.y+p.y);


                        Point parentCoor1 = selectObj.parent.actualCoor;
                        float parentRotate1 = selectObj.parent.rotate + parentRotate;
                        Point selectCoor1 = selectObj.coordinante;

                        double rad1 = Math.toRadians((double)parentRotate1);

                        Point p1 = new Point((int)(selectCoor1.x*Math.cos(rad1) - selectCoor1.y*Math.sin(rad1)),
                                (int)(selectCoor1.x*Math.sin(rad1) + selectCoor1.y*Math.cos(rad1)));

                        selectObj.actualCoor = new Point(parentCoor1.x + p1.x, parentCoor1.y+p1.y);

                        int clockwise = motionEvent.getX()>=selectObj.actualCoor.x ? -1 : 1;
                        float actual_rotate = (float) 180 - getRotateAngle(current,selectObj.actualCoor);
                        float rotate = actual_rotate - clockwise*(selectObj.parent.rotate + selectObj.parent.parent.rotate);
                        rotate = rotate > 35 ? 35 : rotate;
                        rotate = rotate < -35 ? -35:rotate;
                        selectObj.rotate = clockwise * rotate;
                        selectObj.actualRotate = actual_rotate;
                        view.invalidate();
                    }

                }
                return true;
            }
            //else if(motionEvent.getAction() == )
            else {
                return false;
            }
        }
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {


        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            if(selectObj == null) return false;
            if(selectObj.type == DrawObj.Type.LegUpperLeft ||
                    selectObj.type == DrawObj.Type.LegUpperRight) {
                selectObj.scale = mScaleFactor;
                selectObj.children.get(0).children.get(0).scale = 1/mScaleFactor;
                invalidate();
            }
            else if(selectObj.type == DrawObj.Type.LegLowerLeft ||
                    selectObj.type == DrawObj.Type.LegLowerRight){
                selectObj.scale = mScaleFactor;
                selectObj.children.get(0).scale = 1/mScaleFactor;
                invalidate();
            }
            return true;
        }
        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            selectObj = torso.select(new Point((int)scaleGestureDetector.getFocusX(), (int)scaleGestureDetector.getFocusY()));
            if (selectObj == null) return false;
            if(selectObj.type == DrawObj.Type.LegUpperLeft || selectObj.type== DrawObj.Type.LegUpperRight ||
                    selectObj.type == DrawObj.Type.LegUpperLeft || selectObj.type!= DrawObj.Type.LegUpperRight){
                return true;
            }
            return false;
        }
    }

    private float getRotateAngle(Point origin, Point A) {
        double a = A.x - origin.x;
        double b = A.y - origin.y;
        double ab = Math.sqrt(a*a + b*b);
        float rotate = (float)(Math.acos(b/ab)*(180/Math.PI));
        return rotate;
    }


    public void initial(){
        int head_height = 150;
        int head_width = 100;

        int upperArm_length = (int)(1.4*head_height);
        int arm_width = head_width/2;
        int lowerArm_length = (int)(1.2*head_height);

        int upperLeg_length = (int)(1.5*head_height);
        int lowerLeg_length = (int)(1.9*head_height);

        int hand_length = (int)(0.4*head_height);
        int hand_width = hand_length;

        int feet_length = (int)(0.4*head_height);
        int feet_width = (int)head_width;

        int torso_length = (int)(2.4*head_height);
        int torso_width = (int)(2*head_width);


        torso = buildDrawObj(DrawObj.Type.Torso,
                new RectF(0,0,torso_width,torso_length));
        torso.coordinante = new Point(450,300);
        torso.actualCoor = torso.coordinante;

        DrawObj head = buildDrawObj(DrawObj.Type.Head,
                new RectF(-head_width/2,-head_height,head_width/2,0));
        torso.addChild(head, new Point(torso_width/2,-20));

        //left arm
        DrawObj upperArmLeft = buildDrawObj(DrawObj.Type.ArmUpperLeft,
                new RectF(-arm_width/2,0,arm_width/2,upperArm_length));
        upperArmLeft.rotate = 15;
        torso.addChild(upperArmLeft, new Point(0,0));

        DrawObj lowerArmLeft = buildDrawObj(DrawObj.Type.ArmLowerLeft,
                new RectF(-arm_width/2,0,arm_width/2,lowerArm_length));
        upperArmLeft.addChild(lowerArmLeft, new Point(0, upperArm_length));

        DrawObj handleft = buildDrawObj(DrawObj.Type.Hand,
                new RectF(-hand_width/2,0,hand_width/2,hand_length));
        lowerArmLeft.addChild(handleft, new Point(0,lowerArm_length));



        //right arm
        DrawObj upperArmRight = buildDrawObj(DrawObj.Type.ArmUpperRight,
                new RectF(-arm_width/2,0,arm_width/2,upperArm_length));
        upperArmRight.rotate = -15;
        DrawObj lowerArmRight = buildDrawObj(DrawObj.Type.ArmLowerRight,
                new RectF(-arm_width/2,0,arm_width/2,lowerArm_length));
        DrawObj handright = buildDrawObj(DrawObj.Type.Hand,
                new RectF(-hand_width/2,0,hand_width/2,hand_length));
        torso.addChild(upperArmRight, new Point(torso_width,0));
        upperArmRight.addChild(lowerArmRight, new Point(0,upperArm_length));
        lowerArmRight.addChild(handright, new Point(0,lowerArm_length));



        //left leg
        DrawObj upperLegLeft = buildDrawObj(DrawObj.Type.LegUpperLeft,
                new RectF(-30,0,30,upperLeg_length));

        DrawObj lowerLegLeft = buildDrawObj(DrawObj.Type.LegLowerLeft,
                new RectF(-30,0,30,lowerLeg_length));

        DrawObj feetleft = buildDrawObj(DrawObj.Type.Feet,
                new RectF(-feet_width,0,0,feet_length));
        torso.addChild(upperLegLeft, new Point(torso_width/4, torso_length));
        upperLegLeft.addChild(lowerLegLeft, new Point(0,upperLeg_length));
        lowerLegLeft.addChild(feetleft, new Point(0, lowerLeg_length));



        //right leg
        DrawObj upperLegRight = buildDrawObj(DrawObj.Type.LegUpperRight,
                new RectF(-30,0,30,upperLeg_length));
        DrawObj lowerLegRight = buildDrawObj(DrawObj.Type.LegLowerRight,
                new RectF(-30,0,30,lowerLeg_length));
        DrawObj feetright = buildDrawObj(DrawObj.Type.Feet,
                new RectF(0,0,feet_width,feet_length));

        torso.addChild(upperLegRight, new Point(3*torso_width/4,torso_length));
        upperLegRight.addChild(lowerLegRight, new Point(0, upperLeg_length));
        lowerLegRight.addChild(feetright, new Point(0, lowerLeg_length));
    }

    private void updateActudalCoordinate(DrawObj obj) {

        for (DrawObj child : obj.children) {
            child.actualCoor = new Point(obj.actualCoor.x + child.coordinante.x,
                    obj.actualCoor.y + child.coordinante.y);
            updateActudalCoordinate(child);
        }
    }


    private DrawObj buildDrawObj(DrawObj.Type type, RectF rect){
        DrawObj obj = new DrawObj();
        obj.rectF = rect;
        obj.type = type;
        return obj;
    }
}
