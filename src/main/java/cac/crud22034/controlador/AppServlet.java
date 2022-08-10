
package cac.crud22034.controlador;

import cac.crud22034.modelo.Paciente;
import cac.crud22034.modelo.Modelo;
import cac.crud22034.modelo.ModeloFactory;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Properties;

/**
 *
 * @author Mariela Jorio Gnisci
 */
@WebServlet(name = "AppServlet", urlPatterns = {"/app"})
public class AppServlet extends HttpServlet {

    private Modelo model;
    private final String URI_LIST = "listadoPacientes.jsp";
    private final String URI_REMOVE = "WEB-INF/pages/pacientes/borrarPaciente.jsp";
    private final String URI_EDIT = "WEB-INF/pages/pacientes/editarPaciente.jsp";

    @Override
    public void init() throws ServletException {
        this.model = getModelo();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        int elId;
        Paciente pac;
        accion = accion == null ? "" : accion;
        switch(accion) {
            case "edit":
                elId = Integer.parseInt(request.getParameter("id"));
                pac = model.getPaciente(elId);
                request.setAttribute("pacienteAEditar", pac);
                request.setAttribute("yaTieneFoto", !pac.getFoto().contains("no-face"));
                request.getRequestDispatcher(URI_EDIT).forward(request, response);
                break;
            case "remove":
                elId = Integer.parseInt(request.getParameter("id"));
                pac = model.getPaciente(elId);
                request.setAttribute("pacienteABorrar", pac);
                request.getRequestDispatcher(URI_REMOVE).forward(request, response);
                break;
            default:
                HttpSession sesionHttp = request.getSession();
                sesionHttp.setAttribute("listaPacientes", model.getPacientes());
                //request.getRequestDispatcher(URI_LIST).forward(request, response);
                response.sendRedirect(URI_LIST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Paciente pac;
        int elId;
        String accion = request.getParameter("accion");
        accion = accion == null ? "" : accion;
        switch (accion) {
            case "add":
                pac = new Paciente();                
                cargarPacienteSegunParams(pac, request);
                model.addPaciente(pac);
                break;
            case "update":
                elId = Integer.parseInt(request.getParameter("id"));
                pac = model.getPaciente(elId);
                cargarPacienteSegunParams(pac, request);
                model.updatePaciente(pac);
                break;
            case "delete":
                elId = Integer.parseInt(request.getParameter("id"));
                model.removePaciente(elId);
                break;
        }        
        doGet(request, response);
    }
    
    private void cargarPacienteSegunParams(Paciente a, HttpServletRequest request) {
        a.setNombre(request.getParameter("nombre"));
        a.setApellido(request.getParameter("apellido"));
        a.setMail(request.getParameter("mail"));
        a.setFechaNacimiento(request.getParameter("fechaNac"));
        a.setFoto(request.getParameter("fotoBase64"));
    }
    
    private Modelo getModelo() throws ServletException {
        Modelo m = null;
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")) {
            Properties props = new Properties();
            props.load(is);
            String tipoModelo = props.getProperty("tipoModelo");
            m = ModeloFactory.getInstance().crearModelo(tipoModelo);
        } catch (IOException ex) {
            throw new ServletException("Error de E/S al al leer 'config' para iniciar el Servlet", ex);
        }
        return m;
    }
}
