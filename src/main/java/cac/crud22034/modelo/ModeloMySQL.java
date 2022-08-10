package cac.crud22034.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ModeloMySQL implements Modelo {

    private static final String GET_ALL_QUERY = "SELECT * FROM pacientes";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM pacientes WHERE id = ?";
    private static final String ADD_QUERY = "INSERT INTO pacientes VALUES (null, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE pacientes SET nombre=?, apellido=?, fechaNacimiento = ?, mail=?, fotoBase64=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM pacientes WHERE id = ?";

    @Override
    public List<Paciente> getPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(GET_ALL_QUERY);  ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                pacientes.add(rsToPaciente(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al leer alumnos", ex);
        }
        return pacientes;
    }

    @Override
    public Paciente getPaciente(int id) {
        Paciente alu = null;
        try ( Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(GET_BY_ID_QUERY);) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery();) {
                rs.next();
                alu = rsToPaciente(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al leer alumno con ID " + id, ex);
        }
        return alu;
    }

    @Override
    public int addPaciente(Paciente paciente) {
        int regsAgregados = 0;
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(ADD_QUERY);) {
            fillPreparedStatement(ps, paciente);
            regsAgregados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al agregar paciente " + paciente.getNombreCompleto(), ex);
        }
        return regsAgregados;
    }

    @Override
    public int updatePaciente(Paciente paciente) {
        int regsActualizados = 0;
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(UPDATE_QUERY);) {
            fillPreparedStatement(ps, paciente);
            ps.setInt(6, paciente.getId());
            regsActualizados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al editar paciente " + paciente.getNombreCompleto(), ex);
        }
        return regsActualizados;
    }

    @Override
    public int removePaciente(int id) {
        int regsBorrados = 0;
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(DELETE_QUERY);) {
            ps.setInt(1, id);
            regsBorrados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al borrar paciente con ID " + id, ex);
        }
        return regsBorrados;
    }

    private Paciente rsToPaciente(ResultSet rs) throws SQLException {
        int idPac = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String mail = rs.getString("mail");
        String fechaNac = rs.getString("fechaNacimiento");
        String fotoBase64 = rs.getString("fotoBase64");
        return new Paciente(idPac, nombre, apellido, mail, fechaNac, fotoBase64);
    }

    private void fillPreparedStatement(PreparedStatement ps, Paciente paciente) throws SQLException {
        ps.setString(1, paciente.getNombre());
        ps.setString(2, paciente.getApellido());
        ps.setString(3, paciente.getFechaNacimiento());
        ps.setString(4, paciente.getMail());
        ps.setString(5, paciente.getFoto());
    }

}
