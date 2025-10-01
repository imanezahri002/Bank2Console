package org.example.repositories.implementations;

import org.example.database.DatabaseConnection;
import org.example.models.Client;
import org.example.repositories.interfaces.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;


public class ClientRepositoryImpl implements ClientRepository {
    private final Connection connection;

    public ClientRepositoryImpl(){
        this.connection=DatabaseConnection.getInstance();
    }
    @Override
    public boolean createClient(Client client){
        String sql="INSERT INTO \"Client\" (firstname,lastname,cin,phonenumber,address,email,salaire) values (?,?,?,?,?,?,?)";
        try(PreparedStatement stm=connection.prepareStatement(sql)){
            stm.setString(1,client.getFirstName());
            stm.setString(2,client.getLastName());
            stm.setString(3,client.getCin());
            stm.setString(4,client.getTel());
            stm.setString(5,client.getAddress());
            stm.setString(6,client.getEmail());
            stm.setDouble(7,client.getSalaire());

            int rowsInserted=stm.executeUpdate();
            return rowsInserted > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Client> findByCin(String cin){
        String sql="SELECT * FROM \"Client\" WHERE cin= ?";
        try(PreparedStatement stm=connection.prepareStatement(sql)) {
            stm.setString(1, cin);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                UUID id = rs.getObject("id", UUID.class);
                String nom = rs.getString("lastname");
                String prenom = rs.getString("firstname");
                String cindb = rs.getString("cin");
                String email = rs.getString("email");
                String tel = rs.getString("phonenumber");
                String adresse = rs.getString("address");
                Double salaire = rs.getDouble("salaire");

                Client client = new Client(id, prenom, nom, cindb, tel, adresse, email, salaire);
                return Optional.of(client);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
        }

    }

