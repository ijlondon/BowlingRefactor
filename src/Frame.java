/**
 * Created by iLondon on 4/29/16.
 * This is the context class for the state pattern
 */


public class Frame implements FrameState{
    int rollNum;
    int frameNum;
    int pinCount;
    int score;
    Frame nextFrame;
    FrameState state;

    //Constructor which also assigns the frame its number
    public Frame(int frameNum) {
        this.frameNum = frameNum;
        if(frameNum == 10){
            this.setFrameState(new Tenth());
        }
    }

    //Set this frame's state to parameter
    public void setFrameState(FrameState state) {
        this.state = state;
    }

    //Used to initialize all the frames for that bowler
    public void add(Frame next){
        if( nextFrame == null) {
            nextFrame = next;
        }else nextFrame.add(next);
    }

    @Override
    public void addScore(int addedScore) {

    }
}