/* This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.l2jbr.gameserver.ai;

import com.l2jbr.commons.Config;
import com.l2jbr.gameserver.ThreadPoolManager;
import com.l2jbr.gameserver.datatables.NpcWalkerRoutesTable;
import com.l2jbr.gameserver.model.L2Position;
import com.l2jbr.gameserver.model.actor.instance.L2NpcWalkerInstance;
import com.l2jbr.gameserver.model.actor.instance.L2NpcWalkerInstance.L2NpcWalkerAIAccessor;
import com.l2jbr.gameserver.model.entity.database.WalkerRouteNode;

import java.util.List;

public class L2NpcWalkerAI extends MovableAI<L2NpcWalkerAIAccessor> implements Runnable {
    private static final int DEFAULT_MOVE_DELAY = 0;

    private long _nextMoveTime;

    private boolean _walkingToNextPoint = false;

    int _homeX, _homeY, _homeZ;

    private final List<WalkerRouteNode> _route;

    private int _currentPos;

    public L2NpcWalkerAI(L2NpcWalkerAIAccessor accessor) {
        super(accessor);
        _route = NpcWalkerRoutesTable.getInstance().getRouteForNpc(accessor.getActor().getNpcId());
        ThreadPoolManager.getInstance().scheduleAiAtFixedRate(this, 0, 1000);

    }

    @Override
    public void run() {
        onEvtThink();
    }

    @Override
    protected void onEvtThink() {
        if (!Config.ALLOW_NPC_WALKERS) {
            return;
        }

        if (isWalkingToNextPoint()) {
            checkArrived();
            return;
        }

        if (_nextMoveTime < System.currentTimeMillis()) {
            walkToLocation();
        }
    }

    @Override
    protected void onEvtArrivedBlocked(L2Position blocked_at_pos) {
        _log.warn("NpcWalker ID: " + getActor().getNpcId() + ": Blocked at rote position [" + _currentPos + "], coords: " + blocked_at_pos.x + ", " + blocked_at_pos.y + ", " + blocked_at_pos.z + ". Teleporting to next point");

        int destinationX = _route.get(_currentPos).getMoveX();
        int destinationY = _route.get(_currentPos).getMoveY();
        int destinationZ = _route.get(_currentPos).getMoveZ();

        getActor().teleToLocation(destinationX, destinationY, destinationZ, false);
        super.onEvtArrivedBlocked(blocked_at_pos);
    }

    private void checkArrived() {
        int destinationX = _route.get(_currentPos).getMoveX();
        int destinationY = _route.get(_currentPos).getMoveY();
        int destinationZ = _route.get(_currentPos).getMoveZ();

        if ((getActor().getX() == destinationX) && (getActor().getY() == destinationY) && (getActor().getZ() == destinationZ)) {
            String chat = _route.get(_currentPos).getChatText();
            if ((chat != null) && !chat.equals("")) {
                try {
                    getActor().broadcastChat(chat);
                } catch (ArrayIndexOutOfBoundsException e) {
                    _log.info("L2NpcWalkerInstance: Error, " + e);
                }
            }

            long delay = _route.get(_currentPos).getDelay() * 1000;

            if (delay <= 0) {
                delay = DEFAULT_MOVE_DELAY;
                if (Config.DEVELOPER) {
                    _log.warn("Wrong Delay Set in NpcTemplate Walker Functions = " + delay + " secs, using default delay: " + DEFAULT_MOVE_DELAY + " secs instead.");
                }
            }

            _nextMoveTime = System.currentTimeMillis() + delay;
            setWalkingToNextPoint(false);
        }
    }

    private void walkToLocation() {
        if (_currentPos < (_route.size() - 1)) {
            _currentPos++;
        } else {
            _currentPos = 0;
        }

        boolean moveType = _route.get(_currentPos).getRunning();

        if (moveType) {
            getActor().setRunning();
        } else {
            getActor().setWalking();
        }

        int destinationX = _route.get(_currentPos).getMoveX();
        int destinationY = _route.get(_currentPos).getMoveY();
        int destinationZ = _route.get(_currentPos).getMoveZ();

        setWalkingToNextPoint(true);

        setIntention(Intention.AI_INTENTION_MOVE_TO, new L2Position(destinationX, destinationY, destinationZ, 0));
    }

    public int getHomeX() {
        return _homeX;
    }

    public int getHomeY() {
        return _homeY;
    }

    public int getHomeZ() {
        return _homeZ;
    }

    public void setHomeX(int homeX) {
        _homeX = homeX;
    }

    public void setHomeY(int homeY) {
        _homeY = homeY;
    }

    public void setHomeZ(int homeZ) {
        _homeZ = homeZ;
    }

    public boolean isWalkingToNextPoint() {
        return _walkingToNextPoint;
    }

    public void setWalkingToNextPoint(boolean value) {
        _walkingToNextPoint = value;
    }

    private L2NpcWalkerInstance getActor() {
        return getAccessor().getActor();
    }
}
