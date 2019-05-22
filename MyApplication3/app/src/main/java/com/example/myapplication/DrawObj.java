package com.example.myapplication;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import java.util.Vector;

public class DrawObj {
        Type type;
        RectF rectF;
        Vector<DrawObj> children = new Vector<>();
        Point coordinante;
        Point actualCoor;
        float rotate = 0;
        float actualRotate = 0;
        DrawObj parent;
        float scale = 1;

        public enum Type{
                Head,
                Torso,
                ArmUpperLeft,
                ArmUpperRight,
                ArmLowerLeft,
                ArmLowerRight,
                LegUpperLeft,
                LegUpperRight,
                LegLowerLeft,
                LegLowerRight,
                Hand,
                Feet
        }


        public void addChild(DrawObj obj, Point coor){
                children.add(obj);
                obj.coordinante = coor;
                obj.actualCoor = new Point(actualCoor.x+coor.x, actualCoor.y+coor.y);
                obj.parent = this;
        }



        public void draw(Canvas canvas){
                canvas.save();
                canvas.translate(coordinante.x, coordinante.y);
                canvas.rotate(rotate);
                canvas.scale(1, scale);

                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(10);
                if(type == Type.Torso) {
                        canvas.drawRoundRect(rectF,60,60, paint);
                }
                else {
                        canvas.drawOval(rectF, paint);
                }

                for(DrawObj obj : children){
                        obj.draw(canvas);
                        canvas.restore();
                }
        }
        public DrawObj select(Point point){
                Point p1 = new Point(point.x-coordinante.x, point.y-coordinante.y);

                double rad = Math.toRadians((double)-rotate);

                Point p = new Point((int)(p1.x*Math.cos(rad) - p1.y*Math.sin(rad)),
                        (int)(p1.x*Math.sin(rad) + p1.y*Math.cos(rad)));


                if(rectF.contains(p.x, p.y)){
                        return this;
                }
                for(DrawObj obj:children){
                        DrawObj selectobj = obj.select(p);
                        if(selectobj != null){
                                return selectobj;
                        }
                }
                return null;
        }


}
