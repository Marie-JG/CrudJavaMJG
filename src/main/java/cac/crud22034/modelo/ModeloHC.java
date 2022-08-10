package cac.crud22034.modelo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


// Modelo HC (Hard Codeado): Los datos se guardan en la RAM. Solo sirve para testear la app.
public class ModeloHC implements Modelo {

    private List<Paciente>pacientesGuardados;

    public ModeloHC() {
        this.pacientesGuardados = new ArrayList<>();
        crearPacientesFake();
    }

    @Override
    public List<Paciente> getPacientes() {
        return new ArrayList(this.pacientesGuardados); // Copia de la lista original
    }

    @Override
    public Paciente getPaciente(int id) {
        int i = 0;
        Paciente encontrado = null;
        while (i < this.pacientesGuardados.size() && encontrado == null) {
            Paciente a = this.pacientesGuardados.get(i);
            if (a.getId() == id) {
                encontrado = a;
            } else {
                i++;
            }
        }
        if (encontrado == null) {
            throw new RuntimeException("No se encontró paciente con ID " + id);
        }
        return encontrado;
    }

    @Override
    public int addPaciente(Paciente paciente) {
        this.pacientesGuardados.add(paciente);
        return 0;
    }

    @Override
    public int updatePaciente(Paciente a) {
        Paciente target = getPaciente(a.getId());
        int idx = this.pacientesGuardados.indexOf(target);
        this.pacientesGuardados.set(idx, a);
        return 0;
    }

    @Override
    public int removePaciente(int id) {
        Paciente target = getPaciente(id);
        this.pacientesGuardados.remove(target);
        return 0;
    }

    private void crearPacientesFake() {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("carasFake.properties")) {
            Properties props = new Properties();
            props.load(is);
            this.pacientesGuardados.add(new Paciente(1, "Kouza", "Ibrahim (HC)", "kouibr@mailsrv.fake", "1999-06-22", (String) props.get("HOMBRE_1")));
            this.pacientesGuardados.add(new Paciente(2, "Polo", "Irma", "irmapolo@mailsrv.fake", "1991-02-28"));
            this.pacientesGuardados.add(new Paciente(3, "López", "María", "maria_lopez@mailsrv.fake", "1984-03-24", (String) props.get("MUJER_1")));
            this.pacientesGuardados.add(new Paciente(4, "García", "Luis", "luis123@mailsrv.fake", "1998-07-04", (String) props.get("HOMBRE_3")));
            this.pacientesGuardados.add(new Paciente(5, "Gómez", "Sara", "saragomez@mailsrv.fake", "1991-02-28", (String) props.get("MUJER_3")));
            this.pacientesGuardados.add(new Paciente(6, "Ruiz", "Pedro", "ruiz.pedro@mailsrv.fake", "1986-11-13", (String) props.get("HOMBRE_2")));
            this.pacientesGuardados.add(new Paciente(7, "Pérez", "Lía", "lp12@mailsrv.fake", "1968-07-12", (String) props.get("MUJER_2")));
            this.pacientesGuardados.add(new Paciente(8, "Suárez", "Ana", "suanan@mailsrv.fake", "1992-05-16", (String) props.get("MUJER_4")));
            this.pacientesGuardados.add(new Paciente(9, "Mohamed", "Samuel", "samo@mailsrv.fake", "1990-05-14", (String) props.get("HOMBRE_4")));
        } catch (IOException ex) {
            throw new RuntimeException("No se pudieron cargar las caras fake");
        }
    }
}
