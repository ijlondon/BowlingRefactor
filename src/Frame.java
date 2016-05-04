/**
 * Created by iLondon on 4/29/16.
 * This is the context class for the state pattern
 */


public class Frame implements FrameState{
    int rollNum;
    int frameNum;
    int pinCount;
    int roll1;
    int roll2;
    int roll3;
    int score;
    Frame nextFrame;
    FrameState state;

    //Constructor which also assigns the frame its number
    public Frame(int frameNum) {
        this.frameNum = frameNum;
        if(frameNum == 10){
            this.setFrameState(new Tenth());
        }
        rollNum = 0;
        pinCount = 0;
        score = 0;
        roll1 = -1;
        roll2 = -1;
        roll3 = -1;
    }

    //Set this frame's state to parameter
    public void setFrameState(FrameState state) {
        this.state = state;
        state.setFrame(this);
    }

    //Used to initialize all the frames for that bowler
    public void add(Frame next){
        if( nextFrame == null) {
            nextFrame = next;
        }else nextFrame.add(next);
    }

    @Override
    public void setFrame(Frame frame) {
        state.setFrame(frame);
    }

    @Override
    public FrameStatus addRoll(PinsetterEvent pe) {
        return FrameStatus.SUCCESS;
    }

    @Override
    public boolean canRollAgain() {
        return state.canRollAgain();
    }
}