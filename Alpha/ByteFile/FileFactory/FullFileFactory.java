package Alpha.ByteFile.FileFactory;

import Alpha.ByteFile.FileType.File;
import Alpha.ByteFile.FileType.FullFile;
import Alpha.Exceptions.FileException.AlphaInvalidFilePathException;

public class FullFileFactory implements FileFactory {
    @Override
    public File created() {
        FullFile fullFile = new FullFile();
        return fullFile;
    }

    @Override
    public File created(String fileName, String fileType) throws AlphaInvalidFilePathException {
        FullFile fullFile = new FullFile();
        fullFile.setFile(fileName,fileType);
        return fullFile;
    }
}
