package com.sales.tracking.salestracking.Bean;

import java.util.ArrayList;

public class ClientNotificationManagerBean {

public static class Clients_Count{
    public String getTot_leads() {
        return tot_leads;
    }

    public void setTot_leads(String tot_leads) {
        this.tot_leads = tot_leads;
    }

    String tot_leads;
}

    public ArrayList<Clients_Count> getClients_count() {
        return clients_count;
    }

    public void setClients_count(ArrayList<Clients_Count> clients_count) {
        this.clients_count = clients_count;
    }

    public ArrayList<Clients_Count> clients_count;

}
