package model;

public class Cliente {
    private final String nombre;
    private final String apellido;
    private final int edad;
    private final String email;

    public Cliente(String nombre, String apellido, int edad, String email) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Nombre requerido");
        if (apellido == null || apellido.isBlank())
            throw new IllegalArgumentException("Apellido requerido");
        if (edad < 0 || edad > 120)
            throw new IllegalArgumentException("Edad inválida");
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Email inválido");

        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getEdad() {
        return edad;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + email + ")";
    }
}
