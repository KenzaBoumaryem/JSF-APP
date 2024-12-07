package com.sakadream.jsf.bean;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        try {


            EntityManagerFactory emf = Persistence.createEntityManagerFactory("vid_reservation");
            EntityManager em = emf.createEntityManager();
            System.out.println("EntityManager successfully created");
            em.close();
            emf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
