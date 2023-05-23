public class leader_follower {
    private String leader;
    private String follower;

    public leader_follower(String leader, String follower){
        this.leader=leader;
        this.follower=follower;
    }
    public leader_follower(){

    }

    public String getFollower() {
        return follower;
    }

    public String getLeader() {
        return leader;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
}
