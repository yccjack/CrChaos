package com.ycc.register.work;

public enum EnumRetType 
{
	/**
	 *
	 */
    RETURN_OK( 0 ),
	/**
	 *
	 */
    RETURN_FAILED( -1 );
    
    private EnumRetType(int _value ) {
    	this.value = _value;
	}
    
    public int value()
    {
    	return value;
    }
    
    public static EnumRetType fromInt( int value )
    {
    	switch( value )
    	{
    	case -1:
    		return RETURN_FAILED;
    	case 0:
    		return RETURN_OK;
    	default:
    		return null;
    	}
    }
    
    private int value;
}
