package org.insight.cd.exceptions;

import java.sql.SQLException;

public class PersistentLayerException extends SQLException {

	private static final long serialVersionUID = 1L;
	
	
	public PersistentLayerException (String message) {
		super(message);
	}

}
