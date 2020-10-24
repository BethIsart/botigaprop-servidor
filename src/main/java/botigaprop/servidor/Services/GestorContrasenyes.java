package botigaprop.servidor.Services;

import java.security.MessageDigest;

public class GestorContrasenyes {
    public String EncriptarContrasenya(String contrasenya) {

        byte[] plainText = contrasenya.getBytes();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA");

            md.reset();
            md.update(plainText);
            byte[] contrasenyaEncriptada = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < contrasenyaEncriptada.length; i++) {
                if ((contrasenyaEncriptada[i] & 0xff) < 0x10) {
                    sb.append("0");
                }

                sb.append(Long.toString(contrasenyaEncriptada[i] & 0xff, 16));
            }

            return contrasenyaEncriptada.toString();

        } catch (Exception e) {
            return null;
        }
    }
}
