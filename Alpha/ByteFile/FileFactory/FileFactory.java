package Alpha.ByteFile.FileFactory;

import Alpha.ByteFile.FileType.File;
import Alpha.Exceptions.FileException.AlphaInvalidFilePathException;

public interface FileFactory {
    File created();
    File created(String fileName,String fileType) throws AlphaInvalidFilePathException;
}
