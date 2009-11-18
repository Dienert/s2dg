package org.ufpb.s2dg.session.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.ufpb.s2dg.entity.Usuario;

/**
 * Classe de Utilitarios gerais do sistema
 * 
 * @author dienert
 * 
 */
public class Utils {

	/*public static boolean validatePassword(String plainPassword, Usuario user) {
		try {
			MessageDigest digestGen = MessageDigest.getInstance("SHA-1");
			// Check digests
			if (!digestGen
					.isEqual(generateHash(plainPassword), user.getSenha())) {
				System.out.println("Senha invalida");
				return false;
			}
			return true;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
*/
	
	public static boolean validatePasswordPlana(String plainPassword, Usuario user) {

			//MessageDigest digestGen = MessageDigest.getInstance("SHA-1");
			// Check digests
			if (!plainPassword.equals(user.getSenha())) {
				System.out.println("Senha invalida");
				return false;
			}
			return true;
	}
	
	public static byte[] generateHash(String string) {
		char[] password = string.toCharArray();
		// Convertendo char[] para byte[]
		byte[] passwordBytes = new byte[2 * password.length];
		for (int i = 0, count = 0; i < password.length; i++, count += 2) {
			passwordBytes[count] = (byte) (password[i] & 0xff);
			passwordBytes[count + 1] = (byte) (password[i] >> 8 & 0xff);
		}
		try {
			// Gerando o digest do password
			MessageDigest digestGen = MessageDigest.getInstance("SHA-1");
			byte[] passwordDigest = digestGen.digest(passwordBytes);
			return passwordDigest;
		}// Nunca deveria ser lancada
		catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String getHexString(byte[] b) throws Exception {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        long length = file.length();
    
        if (length > Integer.MAX_VALUE) {
            throw new IOException("O arquivo é muito grande: "+file.getName());
        }
        byte[] bytes = new byte[(int)length];
    
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Não foi possível ler o arquivo completamente: "+file.getName());
        }

        is.close();
        return bytes;
    }

}
