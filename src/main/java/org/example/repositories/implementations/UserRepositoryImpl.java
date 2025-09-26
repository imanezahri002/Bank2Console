package org.example.repositories.implementations;

import org.example.database.DatabaseConnection;
import org.example.models.User;
import org.example.repositories.interfaces.UserRepository;

import javax.management.relation.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, email, role,name FROM \"User\" WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getObject("id", UUID.class));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                String roleStr = rs.getString("role");
                user.setRole(User.Role.valueOf(roleStr.toUpperCase()));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override

    public boolean addUser(User user){
        String sql="INSERT INTO \"User\" (name,email,password,role) values (?,?,?,?::role_enum)";
        try (Connection conn=DatabaseConnection.getInstance();
                PreparedStatement stmt=conn.prepareStatement(sql)){

                stmt.setString(1,user.getName());
                stmt.setString(2,user.getEmail());
                stmt.setString(3,user.getPassword());
                stmt.setString(4, user.getRole().name());


                int rowsInserted = stmt.executeUpdate();
                return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
