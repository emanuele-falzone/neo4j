/*
 * Copyright (c) 2002-2019 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.test.limited;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.neo4j.io.fs.StoreChannel;

public class LimitedFileChannel implements StoreChannel
{
    private final StoreChannel inner;
    private final LimitedFilesystemAbstraction fs;

    public LimitedFileChannel( StoreChannel inner, LimitedFilesystemAbstraction limitedFilesystemAbstraction )
    {
        this.inner = inner;
        fs = limitedFilesystemAbstraction;
    }

    @Override
    public int read( ByteBuffer byteBuffer ) throws IOException
    {
        return inner.read( byteBuffer );
    }

    @Override
    public long read( ByteBuffer[] byteBuffers, int offset, int length ) throws IOException
    {
        return inner.read( byteBuffers, offset, length );
    }

    @Override
    public long read( ByteBuffer[] dsts ) throws IOException
    {
        return read( dsts, 0, dsts.length );
    }

    @Override
    public int write( ByteBuffer byteBuffer ) throws IOException
    {
        fs.ensureHasSpace();
        return inner.write( byteBuffer );
    }

    @Override
    public long write( ByteBuffer[] byteBuffers, int offset, int length ) throws IOException
    {
        fs.ensureHasSpace();
        return inner.write( byteBuffers, offset, length );
    }

    @Override
    public long write( ByteBuffer[] srcs ) throws IOException
    {
        return write( srcs, 0, srcs.length );
    }

    @Override
    public long position() throws IOException
    {
        return inner.position();
    }

    @Override
    public LimitedFileChannel position( long newPosition ) throws IOException
    {
        return new LimitedFileChannel( inner.position( newPosition ), fs );
    }

    @Override
    public long size() throws IOException
    {
        return inner.size();
    }

    @Override
    public LimitedFileChannel truncate( long size ) throws IOException
    {
        return new LimitedFileChannel( inner.truncate( size ), fs );
    }

    @Override
    public int getFileDescriptor()
    {
        return inner.getFileDescriptor();
    }

    @Override
    public void force( boolean b ) throws IOException
    {
        fs.ensureHasSpace();
        inner.force( b );
    }

    @Override
    public int read( ByteBuffer byteBuffer, long position ) throws IOException
    {
        return inner.read( byteBuffer, position );
    }

    @Override
    public void readAll( ByteBuffer dst ) throws IOException
    {
        inner.readAll( dst );
    }

    @Override
    public FileLock tryLock() throws IOException
    {
        return inner.tryLock();
    }

    @Override
    public void writeAll( ByteBuffer src, long position ) throws IOException
    {
        fs.ensureHasSpace();
        inner.writeAll( src, position );
    }

    @Override
    public void writeAll( ByteBuffer src ) throws IOException
    {
        fs.ensureHasSpace();
        inner.writeAll( src );
    }

    @Override
    public boolean isOpen()
    {
        return inner.isOpen();
    }

    @Override
    public void close() throws IOException
    {
        inner.close();
    }

    @Override
    public void flush() throws IOException
    {
        inner.flush();
    }

    @Override
    public boolean hasPositionLock()
    {
        return inner.hasPositionLock();
    }

    @Override
    public Object getPositionLock()
    {
        return inner.getPositionLock();
    }

    @Override
    public void tryMakeUninterruptible()
    {
        inner.tryMakeUninterruptible();
    }
}
