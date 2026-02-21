package com.DevUp.DoctorUp.Controller;


import com.DevUp.DoctorUp.Config.JwtUtil;
import com.DevUp.DoctorUp.DTO.*;
import com.DevUp.DoctorUp.Service.*;
//import com.Devup.Appointment.DTO.PatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {




    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserServiceClient userServiceClient;

    private String appointmentServiceUrl="http://APPOINTMENT-SERVICE";


    private final RestTemplate restTemplate;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private JwtUtil jwtUtil;

    public HomeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping({"/", "/home"})
    public String getHome(){
        return "home";
    }

    @GetMapping("/register/patient")
    public String registePatient(Model model){
        RegisterPatientDTO patientDTO =new RegisterPatientDTO();
        model.addAttribute("patient",patientDTO);
        return "register-patient";
    }

    @PostMapping("/register/patient")
    public String patientRegistered(@ModelAttribute RegisterPatientDTO registerPatientDTO) {

        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setName(registerPatientDTO.getName());
        dto.setEmail(registerPatientDTO.getEmail());
        dto.setPassword(registerPatientDTO.getPassword());
        dto.setPhone(registerPatientDTO.getPhone());
        dto.setAge(registerPatientDTO.getAge());
        dto.setGender(registerPatientDTO.getGender());
        dto.setRole("PATIENT");

        userServiceClient.registerUser(dto);

        return "home";
    }

    @GetMapping("/register/doctor")
    public String registerDoctor(Model model) {
        RegisterDoctorDTO registerDoctorDTO=new RegisterDoctorDTO();
        model.addAttribute("doctor",registerDoctorDTO);
        return "register-doctor";
    }

    @PostMapping("/register/doctor")
    public String doctorRegistered(@ModelAttribute RegisterDoctorDTO registerDoctorDTO) {

        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setName(registerDoctorDTO.getName());
        dto.setEmail(registerDoctorDTO.getEmail());
        dto.setPassword(registerDoctorDTO.getPassword());
        dto.setSpecialization(registerDoctorDTO.getSpecialization());
        dto.setRole("DOCTOR");
        System.out.println("Sending to user Service");
        userServiceClient.registerUser(dto);

        return "home";
    }

    @GetMapping("/login")
    public String login(Model model) {
//        LoginDTO loginDTO =new LoginDTO();
        model.addAttribute("user",new LoginDTO());
        return "login";
    }



    @PostMapping("/login")
    public String processLogin(@ModelAttribute LoginDTO loginDTO,
//                               @RequestParam String role,
                               HttpSession session,
                               Model model) {
        try {
            JwtResponseDTO jwtResponse=userServiceClient.loginUser(loginDTO);
//            JwtResponseDTO jwtResponse = authService.authenticateUser(loginDTO, role);

            session.setAttribute("TOKEN", jwtResponse.getToken());
            session.setAttribute("ROLE", jwtResponse.getRole());
            session.setAttribute("LoggedInuUserID",jwtResponse.getId());

            System.out.println("Login Done "+jwtResponse.getRole()+"  "+jwtUtil.extractUsername(jwtResponse.getToken())+ jwtResponse.getId());
            if ("DOCTOR".equalsIgnoreCase(jwtResponse.getRole())) {
                return "redirect:/dashboard/doctor";
            } else {
                return "redirect:/dashboard/patient";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", new LoginDTO());
            model.addAttribute("error", "Invalid credentials or service unavailable");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();//removes everything from session
        }
        return "home";
    }

    @GetMapping("/dashboard/patient")
    public String patientDashboard(Model model, HttpSession session) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) {
            return "redirect:/login";
        }

        String email = jwtUtil.extractUsername(token);
        System.out.println("Patient email in UI: " + email);

        // Get patient info
//        PatientDTO patient = patientService.getPatientByEmail(email, token);
        UserDetailsDTO patient = userServiceClient.getUserbyEmail(email,token);
        System.out.println("Patients Dashboard patient  "+ patient.toString());
        model.addAttribute("patient", patient);

        List<DoctorDTO> doctors = userServiceClient.getAllDoctors(token);
        model.addAttribute("doctors", doctors);

        System.out.println("Doctors  "+ doctors.get(0));


        int userId = (int) session.getAttribute("LoggedInuUserID");
        List<AppointmentDetailsDTO> rawAppointments = userServiceClient.getPatientAppointments(userId, token);


        List<AppointmentDetailsDTO> appointments = new ArrayList<>();
        for (AppointmentDetailsDTO response : rawAppointments) {

            DoctorDTO doctor = new DoctorDTO();
            doctor.setId(response.getDoctor().getId());
            doctor.setName(response.getDoctor().getName());
            doctor.setEmail(response.getDoctor().getEmail());
            doctor.setSpecialization(response.getDoctor().getSpecialization());


            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setId(response.getPatient().getId());
            patientDTO.setName(response.getPatient().getName());
            patientDTO.setEmail(response.getPatient().getEmail());


            AppointmentDetailsDTO dto = new AppointmentDetailsDTO();
            dto.setDoctor(doctor);
            dto.setPatient(patientDTO);
            dto.setAppointmentDateTime(response.getAppointmentDateTime());
            dto.setStatus(response.getStatus());

            appointments.add(dto);
        }

        model.addAttribute("appointments", appointments);
        return "patient-dashboard";
    }

    @PostMapping("/appointments/book")
    public String bookAppointment(
            @RequestParam("doctorId") int doctorId,
            @RequestParam("patientId") int patientId,
            @RequestParam("appointmentDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime appointmentDateTime,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) {
            return "redirect:/login";
        }

        System.out.println(" before BookingDTO call " );
        AppointmentBookingDTO bookingDTO = new AppointmentBookingDTO(doctorId, patientId, appointmentDateTime);

        appointmentService.bookAppointment(bookingDTO, token);

        redirectAttributes.addFlashAttribute("success", "Appointment booked successfully!");
        return "redirect:/dashboard/patient";
    }


    @GetMapping("/dashboard/doctor")
    public String doctorDashboard(Model model, HttpSession session) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) {
            return "redirect:/login";
        }
        String email = jwtUtil.extractUsername(token);
        System.out.println("Doctor email in UI: " + email);

//        DoctorDTO getDoctor=doctorService.getDoctorByEmail(token,email);
        UserDetailsDTO getDoctor = userServiceClient.getUserbyEmail(email,token);
        System.out.println("Doctor Dashboard   "+ getDoctor.toString());
        model.addAttribute("doctor",getDoctor);


        List<AppointmentDetailsDTO> rawAppointments = userServiceClient.getDoctorAppointments(getDoctor.getId(), token);
        model.addAttribute("appointments",rawAppointments);
        return "doctor-dashboard";
    }

    @PostMapping("/appointment/approve")
    public String approveAppointment(@RequestParam("id") int appointmentId, HttpSession session) {
        String token = (String) session.getAttribute("TOKEN");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                appointmentServiceUrl+"/appointments/" + appointmentId + "/approve", // APPOINTMENT-SERVICE URL
                HttpMethod.PUT,
                entity,
                Void.class
        );

        return "redirect:/dashboard/doctor";
    }

    @PostMapping("/appointment/reject")
    public String rejectAppointment(@RequestParam("id") int appointmentId, HttpSession session) {
        String token = (String) session.getAttribute("TOKEN");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                appointmentServiceUrl+"/appointments/" + appointmentId + "/reject",
                HttpMethod.PUT,
                entity,
                Void.class
        );

        return "redirect:/dashboard/doctor";
    }



}
