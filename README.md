# SchedulingApp
This was for a WGU class project

-Scheduling Application README--------------------------------------------------------------------------------------------------------------------

Application Title: 		Scheduling Application

Application Purpose: 	To connect to a MySQL database containing records for customers, divisions, countries, users, appointments, and contacts, 
						and to add, update, and delete customers, and appointments.

Author: 				Daniel Hopp 
Contact: 				danhopp42@gmail.com
Application Version: 	1.0
Date: 					20-NOV-2022

IDE Used: 				IntelliJ IDEA 2022.2.3 (Community Edition) 
						Build #IC-222.4345.14, built on October 5, 2022.
						Runtime version: 17.0.4.1+7-b469.62 amd64
						VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
			
Java SDK Used: 			SE Development Kit jdk-17.0.5
			
JavaFX Used:			javafx-base:17.0.2

MySQL Conn Driver: 		mysql-connector-j-8.0.31.jar


Program Instructions: 	After the program starts, enter a valid user name and password into the fields and click the Login button. The login form's 
						text and its messages are localized in English and French. All login attempts are logged to login_activity.txt located in the
						application's root folder.

						After logging in, a message will inform the user if there is an upcoming appointment within 15 minuets of their local time.

						The main menu navigates to five different forms: Customers, Scheduling, Appointment Schedules by Contact Report, Customer 
						Appointment Totals by Month Report, and Customers by Country Report.

						Customers:
							The user can add, update or delete a customer. A customer cannot be deleted if they have any existing appointments.
							
						Scheduling:
							The user can add, update, or delete an appointment. Appointments have to be within business hours, 08:00 - 22:00 EST,
							appointments cannot be scheduled in the past, and an appointment cannot overlap with another one. The radio buttons will 
							filter the listed appointments by current week, by month, or all appointments.
							
						Appointment Schedules by Contact Report:
							This report shows all appointments assigned to a particular contact. The contact dropdown will filter the report to the 
							selected contact name.
							
						Customer Appointment Totals by Month Report:
							This report shows all customer appointments totaled by month.
							
						Customers by Country Report:
							This report shows all customers and the country they are located in.
							
						Note: The database is running in UTC, and all times displayed in the applicaion (except for the company's business hours) are
						in the user's local time.


Additional Report: 	The additional report added was the Customers by Country Report. This report shows all customers and the country they are located in.
