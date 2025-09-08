package HiyanjongTilungRai.Resume.Model;


import jakarta.persistence.*;



@Entity
public class AboutMe {

    // Id
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        public Long getId(){
            return id;
        }

        public void setId( Long id){
            this.id = id;
        }



        // First Name
        @Column(nullable = false)
        private String firstName;

        public String getFirstName(){
            return firstName ;
        }

        public void setFirstName(String firstName){
            this.firstName = firstName;
        }



        // Middle Name
        @Column(nullable = false)
        private String middleName;

        public String getMiddleName(){
            return middleName;
        }

        public void setMiddleName(String middleName){
            this.middleName = middleName;
        }



        // Last Name
        @Column(nullable = false)
        private String lastName;

        public String getLastName(){
            return lastName;
        }

        public void setLastName(String lastName){
            this.lastName = lastName;
        }

        // Email
        @Column(nullable = false)
        private String email;

        public String getEmail(){
            return email;
        }

        public void setEmail(String email){
            this.email = email;
        }



        // phoneNumber
        @Column(nullable = false)
        private String phoneNumber;

        public String getPhoneNumber(){
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber){
            this.phoneNumber = phoneNumber;
        }



      // Address
        @Column(nullable = false)
        private String address;

        public String getAddress(){
            return address;
        }

        public void setAddress( String address){
            this.address = address;
        }



        //Description
        @Column(nullable = false, length = 1000)
        private String description;

        public String getDescription(){
            return description;
        }

        public void setDescription(String description){
            this.description = description;
        }
}
