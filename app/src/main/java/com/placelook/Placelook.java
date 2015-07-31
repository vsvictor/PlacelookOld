package com.placelook;

import com.placelook.commands.ActiveOperators;
import com.placelook.commands.CreateSlotCommand;
import com.placelook.commands.ListCitiesCommand;
import com.placelook.commands.ListCountriesCommand;
import com.placelook.commands.Login;
import com.placelook.commands.Ping;
import com.placelook.commands.Registration;
import com.placelook.commands.SessionActionCommand;
import com.placelook.commands.SessionCloseCommand;
import com.placelook.commands.SlotAbandonCommand;
import com.placelook.commands.SlotConfirmCommand;
import com.placelook.commands.SlotRequestCommand;
import com.placelook.commands.SlotSearchCommand;
import com.placelook.commands.UserDataGet;
import com.placelook.commands.Welcome;
import com.placelook.data.PlacelookLocation;

import org.apache.log4j.Logger;

/**
 * Created by victor on 16.06.15.
 */
public class Placelook {
    private static final String TAG="Placelook";
    private static int counter;
    private Logger log;
    private MainActivity act;
    public static String sLog;
    public static String sPas;
    public static boolean bSave;

    public Placelook(MainActivity act){
        this.act = act;
        log = this.act.getLogger();
    }
    public void welcome(){
        Welcome command = new Welcome(counter);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void login(){
        Login command = new Login(counter,act.getAccount(),false);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void login(String sLogin, String sPassword, boolean bSave){
        sLog= sLogin;
        sPas = sPassword;
        this.bSave = bSave;
        Login command = new Login(counter,sLog, sPas,false);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void logout(){
       /*
        Logout command = new Logout(counter);
        command.send(this);
        counter++;
        log.info(TAG+":"+"Sended " + command.getName());
        */
        act.getAccount().loadTemp();
        welcome();
    }
    public void registration(){
        Registration command = new Registration(counter,act.getAccount());
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void registration(String sLogin, String sPassword){
        sLog = sLogin;
        sPas = sPassword;
        Registration command = new Registration(counter,sLogin, sPassword);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }

    public void user_data_get(){
        UserDataGet command = new UserDataGet(counter);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void active_operators(){
        ActiveOperators command = new ActiveOperators(counter);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void getCountriesList(){
        ListCountriesCommand command = new ListCountriesCommand(counter);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }

    public void getCitiesList(String country, String city){
        ListCitiesCommand command = new ListCitiesCommand(counter, country, city, false);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }

    public void findIDCity(String country, String city){
        ListCitiesCommand command = new ListCitiesCommand(counter, country, city, true);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void createSlot(PlacelookLocation loc){
        CreateSlotCommand command = new CreateSlotCommand(counter, loc.getCountry(), loc.getIDCity(), loc.getLatitude(), loc.getLongitude(), loc.getGPS());
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void slotSearch(String country, int idCity){
        SlotSearchCommand command = new SlotSearchCommand(counter,0,100,country,idCity);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void slotSearch(String country){
        SlotSearchCommand command = new SlotSearchCommand(counter,0,100,country);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void slotRequest(int idOper, int seconds, String goal){
        SlotRequestCommand command = new SlotRequestCommand(counter,idOper,seconds,goal);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void slotConfirm(int slot){
        SlotConfirmCommand command = new SlotConfirmCommand(counter,slot);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void slotAbandon(int slot){
        SlotAbandonCommand command = new SlotAbandonCommand(counter,slot);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void sessionClose(int idSession){
        SessionCloseCommand command = new SessionCloseCommand(counter,idSession);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void sessionAction(int idSession, String action){
        SessionActionCommand command = new SessionActionCommand(counter, idSession, action);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
    public void responsePing(){
        Ping command = new Ping(counter);
        command.send(act);
        counter++;
        log.info(TAG + ":" + "Sended " + command.getName());
    }
}
