package it.mag.wrongtzap.manager

import it.mag.wrongtzap.controller.web.exception.user.*
import it.mag.wrongtzap.controller.web.request.user.LoginRequest
import it.mag.wrongtzap.controller.web.request.user.NewPasswordRequest
import it.mag.wrongtzap.controller.web.request.user.RegisterRequest
import it.mag.wrongtzap.jwt.Token
import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.service.*
import it.mag.wrongtzap.util.EmailCoroutineScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserManager @Autowired constructor(
    private val userService: UserService,
    private val conversionService: MapperService,
    private val emailService: EmailService,

    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,

    ) {
    private val passwordFormat = Regex("^[\\w+_!()?*\\-\\[\\]{}]{8,20}$")
    private val usernameFormat = Regex("\\w{6,20}")
    private val emailFormat = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z_]+\\.[a-zA-Z]{2,}$")


    //Create method
    fun createUser(request: RegisterRequest): Token{

        //Checks for invalid credentials format
        if(!passwordFormat.matches(request.userPassword))
            throw InvalidPasswordFormatException("Provided password does not conform to standard format")
        if (!usernameFormat.matches(request.userName))
            throw InvalidUsernameFormatException("Provided username does not conform to standard format")
        if (!emailFormat.matches(request.userMail))
            throw InvalidEmailFormatException("Provided email is invalid")

        //check for already existing user
        if(userService.retrieveByEmail(request.userMail) != null)
            throw UserAlreadyExistsException("User already exist")

        val user = User(
            username = request.userName,
            email = request.userMail.lowercase(),
            password = passwordEncoder.encode(request.userPassword)
        )


        userService.saveUser(user)

        val loginRequest = LoginRequest(
            userPassword = request.userPassword,
            userMail = request.userMail
        )

        return login(loginRequest)
    }

    fun login(userCredentials: LoginRequest): Token {

        val user =  userService.retrieveByEmail(userCredentials.userMail)
            ?: throw UserNotFoundException("")

        val authenticated = passwordMatch(userCredentials.userPassword, user.password)

        if(authenticated){
            EmailCoroutineScope.launch {
                emailService.sendLoginNotification(user.email,user.userId)
            }
            val token = jwtUtil.generateToken(user.email)
            return Token(jwt = token)
        }
        else{
            throw UserNotFoundInAuthentication("Email and/or Password are incorrect")
        }
    }

    fun changePassword(userId: String, newPasswordRequest: NewPasswordRequest): ResponseEntity<Any>{
        val user = userService.retrieveById(userId)

        val auth = passwordMatch(newPasswordRequest.oldPassword, user.password)

        if(auth){
            user.apply {
                user.password = passwordEncoder.encode(newPasswordRequest.newPassword)
                userService.saveUser(user)
                return ResponseEntity.status(200).body("Password Changed successfully")
            }
        }
        else throw UserNotFoundInAuthentication("")
    }

    private fun passwordMatch(rawPassword: String, hashPassword: String): Boolean{
        val passwordEncoder = BCryptPasswordEncoder()
        return passwordEncoder.matches(rawPassword, hashPassword)
    }

}
