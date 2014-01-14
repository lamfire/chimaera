package com.lamfire.chimaera;

import com.lamfire.chimaera.command.Command;
import com.lamfire.chimaera.service.Service;
import com.lamfire.chimaera.annotation.COMMAND;
import com.lamfire.chimaera.annotation.SERVICE;
import com.lamfire.logger.Logger;
import com.lamfire.utils.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServiceRegistry {
    private static final Logger LOGGER = Logger.getLogger(ServiceRegistry.class);
    private static final ServiceRegistry  INSTANCE = new ServiceRegistry();

    public static final  ServiceRegistry getInstance(){
        return INSTANCE;
    }

	private final Map<String, Class<?>> commandClassesRegistry = Maps.newHashMap();
	private final Map<String, Service<Command>> commandServiceRegistry = Maps.newHashMap();
    private final HashSet<String> writeProtectedCommands = Sets.newHashSet();
    private final HashSet<String> keyRequiredCommands = Sets.newHashSet();

    private ServiceRegistry(){
        autoCommandRegistry();
        autoServiceRegistry();
    }

    private void autoCommandRegistry(){
        String packageName = Command.class.getPackage().getName();
        try {
            Set<Class<?>> classes = ClassLoaderUtils.getClasses(packageName);
            for(Class<?> clsss : classes){
                COMMAND cmd = clsss.getAnnotation(COMMAND.class);
                if(cmd != null){
                    commandClassesRegistry.put(cmd.name(),clsss);
                    if(cmd.writeProtected()){
                        writeProtectedCommands.add(cmd.name());
                    }
                    if(cmd.keyRequired()){
                        keyRequiredCommands.add(cmd.name());
                    }
                    LOGGER.debug("Registry COMMAND:["+cmd.name()+"] to " + clsss.getName() +",WriteProtected = " + cmd.writeProtected());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            LOGGER.error(e.getMessage(),e);
        }
    }


    private void autoServiceRegistry(){
        String packageName = Service.class.getPackage().getName();
        try {
            Set<Class<?>> classes = ClassLoaderUtils.getClasses(packageName);
            for(Class<?> clsss : classes){
                SERVICE ann = clsss.getAnnotation(SERVICE.class);
                if(ann != null){
                    commandServiceRegistry.put(ann.command(), (Service<Command>)clsss.newInstance());
                    LOGGER.debug("Registry SERVICE:["+ann.command()+"] to " + clsss.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            LOGGER.error(e.getMessage(),e);
        }
    }

	public Class<?> getCommandType(String commandName){
		return commandClassesRegistry.get(commandName);
	}
	
	public Service<Command> getService(Command command){
		return getService(command.getCommand());
	}


    public boolean validateCommand(Command command)throws FailedCommandException{
        if(command == null){
            throw new FailedCommandException("Not supported command.");
        }
        if(command.getStore()==null){
            throw new FailedCommandException("The command property 'store' was required.");
        }

        if(keyRequiredCommands.contains(command.getCommand())){
            if(StringUtils.isBlank(command.getKey())){
                throw new FailedCommandException("The command property 'key' was required.");
            }
        }
        return true;
    }
	
	public Service<Command> getService(String commandName){
		return (Service<Command>)commandServiceRegistry.get(commandName);
	}

    public boolean isWriteProtectedCommand(String commandName ){
        return writeProtectedCommands.contains(commandName);
    }
}
