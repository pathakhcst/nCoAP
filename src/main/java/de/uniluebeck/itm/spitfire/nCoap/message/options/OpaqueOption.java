package de.uniluebeck.itm.spitfire.nCoap.message.options;

import de.uniluebeck.itm.spitfire.nCoap.message.options.OptionRegistry.OptionName;
import de.uniluebeck.itm.spitfire.nCoap.toolbox.ByteArrayWrapper;
import de.uniluebeck.itm.spitfire.nCoap.message.options.OptionRegistry.OptionType;
import de.uniluebeck.itm.spitfire.nCoap.toolbox.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * This class contains all specific functionality for {@link Option} instances of {@link OptionType#OPAQUE}. If there is
 * any need to access {@link Option} instances directly, e.g. to retrieve its value, one could either cast the option
 * to {@link OpaqueOption} and call {@link #getDecodedValue()} or one could call {@link Option#getDecodedValue()} and
 * cast the return value to {@link String}.
 *
 * @author Oliver Kleine
 */
class OpaqueOption extends Option{

    private static Logger log = LoggerFactory.getLogger(OpaqueOption.class.getName());
    
    //Constructor with package visibility
    OpaqueOption(OptionName optionName, byte[] value) throws InvalidOptionException{
        super(optionName);
        //Check whether current number is appropriate for an OpaqueOption
        if(OptionRegistry.getOptionType(optionName) != OptionRegistry.OptionType.OPAQUE){
            String msg = optionName + " is no opaque option.";
            throw new InvalidOptionException(optionNumber, msg);
        }

        setValue(optionName, value);

        log.debug("{} option with value {} created.", optionName, Tools.toHexString(value));
    }

    private void setValue(OptionName optionName, byte[] value) throws InvalidOptionException{

        int min_length = OptionRegistry.getMinLength(optionName);
        int max_length = OptionRegistry.getMaxLength(optionName);

        //Check whether length constraints are fulfilled
        if(value.length < min_length || value.length > max_length){
            String msg = "Value length for " + OptionName.getByNumber(optionNumber) + " must be between " +
                    min_length + " and " +  max_length + " but is " + value.length;
            throw new InvalidOptionException(optionNumber, msg);
        }

        //Set value if there was no Exception thrown so far
        this.value = value;
    }

    /**
     * This method is just to implement the satisfy the {@link Option} abstract method from {@link Option}.
     * The return value is exactly the same as {@link #getValue()}.
     *
     * @return the byte[] containing the options value
     */
    @Override
    public byte[] getDecodedValue(){
       return getValue();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof OpaqueOption)){
            return false;
        }
        OpaqueOption opt = (OpaqueOption) o;
        if((optionNumber == opt.optionNumber) && Arrays.equals(this.value, opt.value)){
            return true;
        }
        return false;
    }
}
