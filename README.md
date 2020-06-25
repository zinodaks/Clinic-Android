# Clinic Appointments & Consultations - Android

### Overview

After building a website with the same concept that can be found [here](https://github.com/zinodaks/Clinic-PHP), it was worth the challenge to create a fully functioncal Android application with a PHP backend that commincates directly with a database and allows users to login and register. The application also provides a different view for the two types of users that the application can have (scroll down to Roles). 

The backend was not only important for authentication but also for learning RecyclerViews in android along with how to make network requests from an android application to a remote database.

**Security was not the focus here as the application will never be deployed**

### Concept

The concept of the application is to allow patients who are feeling sick to take appointments and describe their symptoms at a clinic. The clinic manager/doctor is in turn able to consult these patients on the days of their appointments and prescribe medication for them or cancel their appointments.

## Roles

### Patient

A patient is able to register and login to the system in order to take an appointment at a certain date to visit the clinic. The patient chooses the date and describes their symptoms. A patient is also able to view the consultations/prescriptions written for them at previous appointments at the clinic.

### Doctor / Clinic Manager

A doctor can login to the system in order to view the appointments. An appointment can be cancelled by the doctor. A prescription/consultation can be also be made for a patient , the doctor can enter a medicine name (with AJAX live suggestions from the medicines table in the database) along with directions on how to take these medications that the patient can later view from their personal accounts.
