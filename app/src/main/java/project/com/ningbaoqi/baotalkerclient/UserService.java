package project.com.ningbaoqi.baotalkerclient;

public class UserService implements IUserService{
    @Override
    public String search(int code){
        return "User" + code;
    }
}
