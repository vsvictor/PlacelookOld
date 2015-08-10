package com.placelook.commands;

/**
 * Created by victor on 09.08.15.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.placelook.MainActivity;
import com.placelook.NetService;
import com.placelook.Placelook;
import com.placelook.R;
import com.placelook.data.IntStringPair;

public class Error extends IntStringPair{
    private Context context;
    private String command;

    public enum Critical {NON_CRITICAL, CRITICAL, MAIN, EXIT};
    private Critical critical;
    public Error(Context context){
        this(context,10);
    }
    public Error(Context context, int code){
        super(code);
        this.context = context;
        switch (code){
            case -7:{setName(context.getResources().getString(R.string.sock_lost));critical = Critical.MAIN;break;}
            case -6:{setName(context.getResources().getString(R.string.sock_server));critical = Critical.MAIN;break;}
            case -5:{setName(context.getResources().getString(R.string.sock_internal));critical = Critical.MAIN;break;}
            case -4:{setName(context.getResources().getString(R.string.sock_protocol));critical = Critical.MAIN;break;}
            case -3:{setName(context.getResources().getString(R.string.sock_prev));critical = Critical.MAIN;break;}
            case -2:{setName(context.getResources().getString(R.string.sock_could));critical = Critical.MAIN;break;}

            case 2:{setName(context.getResources().getString(R.string.err_access));critical = Critical.CRITICAL;break;}
            case 3:{setName(context.getResources().getString(R.string.err_auth));critical = Critical.CRITICAL;break;}
            case 4:{setName(context.getResources().getString(R.string.err_loggined));critical = Critical.CRITICAL;break;}
            case 5:{setName(context.getResources().getString(R.string.err_command));critical = Critical.MAIN;break;}
            case 6:{setName(context.getResources().getString(R.string.err_unavailable));critical = Critical.NON_CRITICAL;break;}
            case 7:{setName(context.getResources().getString(R.string.err_server));critical = Critical.CRITICAL;break;}
            case 8:{setName(context.getResources().getString(R.string.err_syntax));critical = Critical.CRITICAL;break;}
            case 9:{setName(context.getResources().getString(R.string.err_blocked));critical = Critical.CRITICAL;break;}
            case 10:{setName(context.getResources().getString(R.string.err_unknown));critical = Critical.CRITICAL;break;}
            case 11:{setName(context.getResources().getString(R.string.err_user));critical = Critical.CRITICAL;break;}
            case 12:{setName(context.getResources().getString(R.string.err_version));critical = Critical.EXIT;break;}
            case 13:{setName(context.getResources().getString(R.string.err_unknowncommand));critical = Critical.MAIN;break;}
            case 14:{setName(context.getResources().getString(R.string.err_network));critical = Critical.NON_CRITICAL;break;}
            case 15:{setName(context.getResources().getString(R.string.err_funds));critical = Critical.MAIN;break;}
            case 16:{setName(context.getResources().getString(R.string.err_virify));critical = Critical.MAIN;break;}
            case 17:{setName(context.getResources().getString(R.string.err_registry));critical = Critical.MAIN;break;}
            default:{setName(context.getResources().getString(R.string.err_unknown));critical = Critical.MAIN;break;}
        }
    }
    public void setTextCommand(String command){
        this.command = command;
    }
    public Critical view(){
        AlertDialog dial = null;
        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle(context.getResources().getString(R.string.error));
        ad.setMessage(getName());
        if(critical == Critical.NON_CRITICAL){
            ad.setPositiveButton(context.getResources().getString(R.string.repeat), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, NetService.class);
                    intent.putExtra("obj",MainActivity.last_command);
                    context.startService(intent);
                    dialog.cancel();
                }
            });
            ad.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        else if(critical == Critical.CRITICAL){
            ad.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        else if(critical == Critical.MAIN){
            ad.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }
        else {
            ad.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            System.exit(-1);
        }
        dial = ad.create();
        dial.show();
        return critical;
    }
}
